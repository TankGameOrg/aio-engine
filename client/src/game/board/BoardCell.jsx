import "./BoardCell.css"

function BoardCell({unit, floor, selectMode, enabled, onClick}) {
    const unitClass = unit?.class;
    const floorClass =  floor?.class;

    let text;
    let background;

    if (unitClass === undefined) {
        text = "";
    } else if (unitClass === "Tank") {
        text = unit?.$PLAYER_REF?.name;
    } else if (unitClass === "Wall") {
        text = unit?.$DURABILITY;
    } else {
        text = "Unknown"
    }

    if (floorClass === "GoldMine") {
        background = "board-cell-background-gold"
    } else {
        background = "board-cell-background-gray"
    }

    let cellEnabledClass = "";
    if (selectMode) {
        if (enabled) {
            cellEnabledClass = "board-cell-enabled";
        } else {
            cellEnabledClass = "board-cell-disabled";
        }
    }

    return (
        <div className={`board-cell ${background} ${cellEnabledClass}`} onClick={() => {
            if (enabled) {
                onClick();
            }
        }}>
            <span className="board-cell-content">{text}</span>
        </div>
    );
}

export default BoardCell;
