import {useState} from "react";
import {positionToString} from "../util/position.js";
import "./ActionSelector.css"

function ParameterSelector({parameter, callback, handlePositionSelection, selectPositionFunction}) {
    const DEFAULT_SELECTED_POSITION = {};
    const [selectedPosition, setSelectedPosition] = useState(DEFAULT_SELECTED_POSITION);

    selectPositionFunction.current = ((x, y) => {
        setSelectedPosition({x, y});
        callback({...parameter, selected: {x, y, class: "Position"}});
    });

    let selector = <div>Blank Selector</div>;

    function present(parameter, value) {
        if (parameter.type === "Integer" || parameter.type === "String" || parameter.type === "Boolean") {
            return value;
        } else if (parameter.type === "PlayerRef") {
            return value?.name;
        } else if (parameter.type === "Specialty" || parameter.type === "Boon") {
            return value?.name;
        } else {
            return `Unhandled type ${parameter.type}`;
        }
    }

    const presentationToValue = {};
    parameter.values.forEach((value) => presentationToValue[present(parameter, value)] =  value);


    if (parameter.bound === "DISCRETE") {
        if (parameter.type === "Position") {
            selector = <button
                className="position-select-button"
                onClick={() => handlePositionSelection()}
            >
                {positionToString(selectedPosition) ?? "Select"}
            </button>;
        } else {
            selector = (
                <select className="selector" defaultValue="default" onChange={(event) => {
                    const presentation = event.target.value;
                    const value = presentationToValue[presentation];
                    callback({...parameter, selected: value});
                }}>
                    <option value="default" disabled></option>
                    {parameter.values.map((value, index) => {
                        return (
                            <option key={index} value={present(parameter, value)}>{present(parameter, value)}</option>
                        );
                    })}
                </select>
            );
        }
    } else if (parameter.bound === "RANGE") {
        // USE SPINNER
        selector = "Unimplemented";
    } else {
        // ERROR
        selector = "Unknown parameter bound type " + parameter.bound;
    }

    return (
        <div className="select-margin">
            <span>{parameter.name}: {selector}</span>
        </div>
    );
}

export default ParameterSelector;
