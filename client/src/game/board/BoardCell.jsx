import "./BoardCell.css"
import Popup from "reactjs-popup";
import {codeObjectToString, ENGLISH_LOCALE, objectToArray, objectToLocalizedObject} from "../../util/locale.js";

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

    const cell = (
        <div className={`board-cell ${background} ${cellEnabledClass}`} onClick={() => {
            if (selectMode && enabled) {
                onClick();
            }
        }}>
            <span className="board-cell-content">{text}</span>
        </div>
    );

    if (selectMode) {
        return cell;
    }


    let unitAttributes = [];
    if (unit.class) {
        unitAttributes = objectToArray(objectToLocalizedObject(ENGLISH_LOCALE, unit));
    }

    // let floorAttributes = [];
    // if (floor.class) {
    //     floorAttributes = objectToArray(objectToLocalizedObject(ENGLISH_LOCALE, floor));
    // }

    return (
        <Popup position="top center" trigger={cell} arrow={false}>
            <div className="popup-overlay"/>
            <div className="popup-container">
                <span className="popup-title">{unit?.class ?? "Empty"}</span>
                { unitAttributes.map((pair) => <span key={pair.key}>{`${pair.key}: ${codeObjectToString(pair.value)}`}</span>) }
                {floor?.class ? <span className="popup-title">{floor.class}</span> : <></>}

            </div>
        </Popup>
    );


}

export default BoardCell;
