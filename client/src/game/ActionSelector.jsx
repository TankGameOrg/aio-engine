import {fetchPossibleActions, postAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useEffect, useState} from "react";
import ParameterSelector from "./ParameterSelector.jsx";
import "./ActionSelector.css"

function ActionSelector({players, uuid, enabled, update, setPositionOptions, selectPositionFunction}) {
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

    function getPossibleActions(playerName) {
        fetchPossibleActions(SERVER_URL, uuid, playerName).then(res => res.json()).then(data => {
            setPossibleActions(data);
        });
    }

    useEffect(() => {
        if (player !== DEFAULT_STRING) {
            getPossibleActions(player);
        }
    }, [player]);

    return (
        <div>
            <div className="player-select select-margin">
                { players.map((playerObject) =>
                    <div key={playerObject.$NAME}>
                        <input type="radio" name="player" value={playerObject.$NAME} id={playerObject.$NAME} onClick={() => setPlayer(playerObject.$NAME)} />
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
                               onClick={() => setSelectedAction(action)}
                        />
                        <label title={action.description}>{action.name}</label>
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
                        }}
                        key={parameter.name}
                        handlePositionSelection={() => setPositionOptions(positionsToReport)}
                        selectPositionFunction={selectPositionFunction}
                    />
                })}
            </div>
            { selectedAction.name === DEFAULT_STRING ? <></> :
                <button disabled={ Object.keys(chosenActionParameters).length !== selectedAction.parameters.length } onClick={
                    () => {
                        const entry = {};
                        Object.values(chosenActionParameters).map((parameter) => entry[parameter.attribute] = parameter.selected);
                        entry.action = selectedAction.name;
                        entry.subject = {
                            name: player,
                            class: "PlayerRef",
                        };
                        postAction(SERVER_URL, uuid, entry).then((res) => res.json()).then((json) => {
                            if (json.error) {
                                console.log(json.message);
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
        </div>
    );
}

export default ActionSelector;
