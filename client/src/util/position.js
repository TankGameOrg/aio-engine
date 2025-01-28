function positionToString(position) {
    if (position.x === undefined || position.y === undefined) {
        return undefined;
    }

    return String.fromCharCode(65 + position.x) + (position.y + 1);
}

function stringToPosition(string) {
    if (string === undefined || string === null || string === "") {
        return undefined;
    }

    return {
        x: (string.charCodeAt(0) - 65),
        y: (Number(string.slice(1)) - 1),
        class: "Position"
    };
}

export { positionToString, stringToPosition };
