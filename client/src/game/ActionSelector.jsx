import {fetchPossibleActions, postAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useEffect, useState} from "react";
import ParameterSelector from "./ParameterSelector.jsx";

function ActionSelector({players, uuid, enabled, update}) {
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
            console.log(data);
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
            <div className="player-select">
                { players.map((playerObject) =>
                    <div key={playerObject.$NAME}>
                        <input type="radio" name="player" value={playerObject.$NAME} id={playerObject.$NAME} onClick={() => setPlayer(playerObject.$NAME)} />
                        <span>{playerObject.$NAME}</span>
                    </div>
                )}
            </div>
            <div className="action-select">
                { possibleActions.possible_actions?.map((action) =>
                    <div key={action.name}>
                        <input type="radio" name="action" value={action.name} id={action.name} onClick={() => {setSelectedAction(action)}} />
                        <label title={action.description}>{action.name}</label>
                    </div>
                )}
            </div>
            <div className="action-parameters">
                { selectedAction.parameters?.map((parameter) => <ParameterSelector parameter={parameter} callback={(parameter) => {
                    const newChosenActionParameters = {...chosenActionParameters};
                    newChosenActionParameters[parameter.name] = parameter;
                    setChosenActionParameters(newChosenActionParameters);
                }} key={parameter.name} />) }
            </div>
            <button disabled={ selectedAction.name === DEFAULT_STRING || Object.keys(chosenActionParameters).length !== selectedAction.parameters.length} onClick={
                () => {
                    const entry = {};
                    Object.values(chosenActionParameters).map((parameter) => entry[parameter.attribute] = parameter.selected);
                    entry.action = selectedAction.name;
                    entry.subject = {
                        name: player,
                        class: "PlayerRef",
                    };
                    console.log(entry);
                    postAction(SERVER_URL, uuid, entry).then(() => update());
                    setPlayer(DEFAULT_STRING);
                    setPossibleActions(DEFAULT_POSSIBLE_ACTIONS);
                    setSelectedAction(DEFAULT_SELECTED_ACTION);
                    setChosenActionParameters(DEFAULT_CHOSEN_ACTION_PARAMETERS);
                }
            }>Submit!</button>
        </div>
    );
}

export default ActionSelector;
