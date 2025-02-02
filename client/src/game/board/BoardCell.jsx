import "./BoardCell.css"
import Popup from "reactjs-popup";
import {codeObjectToString, ENGLISH_LOCALE, objectToArray, objectToLocalizedObject} from "../../util/locale.js";
import {useState} from "react";

function BoardCell({unit, floor, gameIsCurrent, selectMode, enabled, onClick, selectPlayer}) {

    const [popupOpen, setPopupOpen] = useState(false);

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
            } else {
                setPopupOpen(true);
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

    return (
        <>
            { cell }
            <Popup position="bottom center"  open={popupOpen} arrow={false} closeOnDocumentClick={true} onClose={() => setPopupOpen(false)}>
                <div className="popup-overlay" />
                <div className="popup-container">
                    <span className="popup-title">{unit?.class ?? "Empty"}</span>
                    { unitAttributes.map((pair) => <span key={pair.key}>{`${pair.key}: ${codeObjectToString(pair.value)}`}</span>) }
                    {floor?.class ? <span className="popup-title">{floor.class}</span> : <></>}
                    { (unit?.$PLAYER_REF && gameIsCurrent) ? <button onClick={() => {
                        selectPlayer.current(unit?.$PLAYER_REF?.name);
                        setPopupOpen(false);
                    }}>Act</button> : <></> }
                </div>
            </Popup>
        </>
    );


}

export default BoardCell;
