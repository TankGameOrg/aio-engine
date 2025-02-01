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
            return value?.variant;
        } else {
            return `Unhandled type ${parameter.type}`;
        }
    }

    if (parameter.bound === "DISCRETE") {
        if (parameter.type === "Position") {
            selector = <button
                onClick={() => handlePositionSelection()}
            >
                {positionToString(selectedPosition) ?? "Select"}
            </button>;
        } else {
            selector = (
                <>
                    {parameter.values.map((value) => {
                        return (
                            <div key={value}>
                                <input type="radio" name={parameter.name} value={value} onClick={() => callback({...parameter, selected: value})} />
                                <label>{present(parameter, value)}</label>
                            </div>
                        );
                    })}
                </>
            );
        }
    } else if (parameter.bound === "RANGE") {
        // USE SPINNER
        selector = "Unimplemented";
    } else {
        // ERROR
        selector = "Unknown parameter bound type "+ parameter.bound;
    }

    return (
        <div className="select-margin">
            <span>{parameter.name}: {selector}</span>
        </div>
    );
}

export default ParameterSelector;
