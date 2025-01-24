function present(parameter, value) {
    if (parameter.type === "Integer" || parameter.type === "String") {
        return value;
    } else if (parameter.type === "PlayerRef") {
        return value?.name;
    } else {
        return `Unhandled type ${parameter.type}`;
    }
}

function ParameterSelector({parameter, callback}) {
    let selector = <div>Blank Selector</div>;

    if (parameter.bound === "DISCRETE") {
        selector = (
            <>
                {parameter.values.map((value) => {
                    return (
                        <div key={value}>
                            <input type="radio" name={parameter.name} value={value} onClick={
                                () => callback({...parameter, selected: value})}
                            />
                            <label>{present(parameter, value)}</label>
                        </div>
                    );
                })}
            </>
        );
    } else if (parameter.bound === "RANGE") {
        // USE SPINNER
        selector = "Unimplemented";
    } else {
        // ERROR
        selector = "Unknown parameter bound type "+ parameter.bound;
    }

    return (
        <div>
            <span>{parameter.name}: {selector}</span>
        </div>
    );
}

export default ParameterSelector;
