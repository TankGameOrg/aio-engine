
function toBoardSpace(element) {

    const type = element.class;

    if (type === undefined) {
        return "Empty";
    }

    if (type === "Tank") {
        return element?.$PLAYER_REF?.name ?? "UnknownTankName"
    } else if (type === "Wall") {
        return element?.$DURABILITY ?? "UnknownDurabilityWall"
    }

    return "Empty"
}

function Board({board}) {
    const { units, floors, width, height } = board;

    if (width === 0 || height === 0) {
        return (<span>Loading...</span>);
    }

    console.log(units);

    const unitBoard = Array.from({length: height},
        () => Array.from({length: width}, () => ({}))
    );

    const floorBoard = Array.from({length: height},
        () => Array.from({length: width}, () => ({}))
    );

    for (let unit of units) {
        const { x, y } = unit.$POSITION;
        unitBoard[y][x] = unit;
    }

    for (let floor of floors) {
        const { x, y } = floor.$POSITION;
        floorBoard[y][x] = floor;
    }

    return (
        <div>
            { unitBoard.map((row, rowIndex) =>
                <div key={rowIndex}>
                    { row.map((element, columnIndex) => <span key={`${rowIndex}:${columnIndex}`}> { toBoardSpace(element) } </span>) }
                </div>
            )}
        </div>
    );
}

export default Board;
