import {fetchPossibleActions, postAction, validateActionParameters} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useState} from "react";
import ParameterSelector from "./ParameterSelector.jsx";
import "./ActionSelector.css"

function ActionSelector({uuid, enabled, update, updateActiveGame, setPositionOptions, selectPositionFunction, selectPlayerForActionFunction, clearActionSelectorFunction}) {
    if (!enabled) {
        return (<></>);
    }

    const DEFAULT_STRING = "";
    const DEFAULT_POSSIBLE_ACTIONS = {
        possible_actions: [],
    };
    const DEFAULT_SELECTED_ACTION = {
        name: DEFAULT_STRING,
        description: DEFAULT_STRING,
        parameters: [],
    };
    const DEFAULT_CHOSEN_ACTION_PARAMETERS = {};

    const [player, setPlayer] = useState(DEFAULT_STRING);
    const [possibleActions, setPossibleActions] = useState(DEFAULT_POSSIBLE_ACTIONS);
    const [selectedAction, setSelectedAction] = useState(DEFAULT_SELECTED_ACTION);
    const [chosenActionParameters, setChosenActionParameters] = useState(DEFAULT_CHOSEN_ACTION_PARAMETERS);
    const [error, setError] = useState(DEFAULT_STRING);

    useEffect(() => {
        selectPlayerForActionFunction.current = (player) => {
            setPlayer(player);
            setSelectedAction(DEFAULT_SELECTED_ACTION);
            setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
            setError(DEFAULT_STRING);
        };

        clearActionSelectorFunction.current = () => {
            setPlayer(DEFAULT_STRING);
            setPossibleActions(DEFAULT_POSSIBLE_ACTIONS);
            setSelectedAction(DEFAULT_SELECTED_ACTION);
            setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
            setError(DEFAULT_STRING);
        };
    }, []);

    function getPossibleActions(playerName) {
        fetchPossibleActions(SERVER_URL, uuid, playerName).then(res => res.json()).then(data => {
            setPossibleActions(data);
        });
    }

    const createLogEntry = useCallback(() => {
        const entry = {};
        Object.values(chosenActionParameters).map((parameter) => entry[parameter.attribute] = parameter.selected);
        entry.action = selectedAction.name;
        entry.subject = {
            name: player,
            class: "PlayerRef",
        };
        return entry;
    }, [chosenActionParameters, selectedAction, player]);

    useEffect(() => {
        if (player !== DEFAULT_STRING) {
            getPossibleActions(player);
        }
    }, [player]);

    useEffect(() => {
        if (player !== DEFAULT_STRING && selectedAction.name !== DEFAULT_STRING && Object.keys(chosenActionParameters).length > 0) {
            const entry = createLogEntry();
            validateActionParameters(SERVER_URL, uuid, entry).then(res => res.json()).then((json) => {
                if (json.error) {
                    setError(json.message);
                }
            });
        }
    }, [player, selectedAction, chosenActionParameters]);

    return (
        <div className="selector-container">
            { player !== DEFAULT_STRING && <div>
                <h2 className="select-action-text">Select an action for {player}</h2>
                <button className="close-button" onClick={() => {
                    setPositionOptions([]);
                    clearActionSelectorFunction.current();
                }}>Close</button>
            </div> }
            <div className="selection">
                <div className="action-select select-margin">
                    { (player !== DEFAULT_STRING && possibleActions.possible_actions.length === 0) ? <span>You have no available actions</span> : possibleActions.possible_actions?.sort((a, b) => a.name.localeCompare(b.name))?.map((action) =>
                        <div key={action.name} className="select-margin">
                            <input type="radio"
                                   name="action"
                                   value={action.name}
                                   id={action.name}
                                   disabled={action.error}
                                   onClick={() => {
                                       setPositionOptions([]);
                                       setSelectedAction(action);
                                       setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
                                       setError(DEFAULT_STRING);
                                   }}
                            />
                            <span>
                                <label title={action.description} className={`${action.error ? "label-strikethrough" : ""}`}>{`${action.name}`}</label>
                                { action.error ? <span>{action.error}</span> : <></> }
                            </span>
                        </div>
                    )}
                </div>
                <div className="action-parameters select-margin">
                    { selectedAction.parameters?.map((parameter) => {
                        let positionsToReport = [];
                        if (parameter.type === "Position") {
                            positionsToReport = parameter.values;
                        }
                        return <ParameterSelector
                            parameter={parameter}
                            callback={(parameter) => {
                                const newChosenActionParameters = {...chosenActionParameters};
                                newChosenActionParameters[parameter.name] = parameter;
                                setChosenActionParameters(newChosenActionParameters);
                                setError(DEFAULT_STRING);
                            }}
                            key={parameter.name}
                            handlePositionSelection={() => setPositionOptions(positionsToReport)}
                            selectPositionFunction={selectPositionFunction}
                        />
                    })}
                </div>
            </div>
            { selectedAction.name === DEFAULT_STRING ? <></> :
                <button disabled={ (Object.keys(chosenActionParameters).length !== selectedAction.parameters.length) || (error !== DEFAULT_STRING)} onClick={
                    () => {
                        const entry = createLogEntry();
                        postAction(SERVER_URL, uuid, entry).then((res) => res.json()).then((json) => {
                            if (json.error) {
                                setError(json.message);
                            } else {
                                update();
                                updateActiveGame();
                            }
                        });
                        setPlayer(DEFAULT_STRING);
                        setPossibleActions(DEFAULT_POSSIBLE_ACTIONS);
                        setSelectedAction(DEFAULT_SELECTED_ACTION);
                        setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
                    }
                }>Submit!</button>
            }
            { error === DEFAULT_STRING ? <></> :
                <p>
                    Error: {error}
                </p>
            }
        </div>
    );
}

export default ActionSelector;
