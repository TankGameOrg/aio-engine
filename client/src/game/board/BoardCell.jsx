import {useState} from "react";
import "./BoardCell.css"

function BoardCell({unit, floor, onClick}) {

    const [popup, setPopup] = useState(false);

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

    return (
        <div className={`board-cell ${background}`} onMouseEnter={() => setPopup(true)} onMouseLeave={() => setPopup(false)}>
            <span className="board-cell-content">{text}</span>
        </div>
    );
}

export default BoardCell;
