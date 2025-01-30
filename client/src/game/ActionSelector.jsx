import {fetchPossibleActions, postAction, validateActionParameters} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useState} from "react";
import ParameterSelector from "./ParameterSelector.jsx";
import "./ActionSelector.css"

function ActionSelector({players, uuid, enabled, update, setPositionOptions, selectPositionFunction, clearActionSelectorFunction}) {
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
        clearActionSelectorFunction.current = () => {
            setPlayer(DEFAULT_STRING);
            setPossibleActions(DEFAULT_POSSIBLE_ACTIONS);
            setSelectedAction(DEFAULT_SELECTED_ACTION);
            setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
            setError(DEFAULT_STRING);
        }
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
        <div>
            <div className="player-select select-margin">
                { players.map((playerObject) =>
                    <div key={playerObject.$NAME}>
                        <input type="radio" name="player" value={playerObject.$NAME} id={playerObject.$NAME} onClick={() => {
                            setPlayer(playerObject.$NAME);
                            setSelectedAction(DEFAULT_SELECTED_ACTION);
                            setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
                            setError(DEFAULT_STRING);
                        }} />
                        <span>{playerObject.$NAME}</span>
                    </div>
                )}
            </div>
            <div className="action-select select-margin">
                { possibleActions.possible_actions?.map((action) =>
                    <div key={action.name} className="select-margin">
                        <input type="radio"
                               name="action"
                               value={action.name}
                               id={action.name}
                               disabled={action.error}
                               onClick={() => {
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
            { selectedAction.name === DEFAULT_STRING ? <></> :
                <button disabled={ (Object.keys(chosenActionParameters).length !== selectedAction.parameters.length) || (error !== DEFAULT_STRING)} onClick={
                    () => {
                        const entry = createLogEntry();
                        postAction(SERVER_URL, uuid, entry).then((res) => res.json()).then((json) => {
                            if (json.error) {
                                setError(json.message);
                            } else {
                                update()
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
