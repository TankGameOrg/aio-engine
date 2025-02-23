import "./Menu.css"
import {useState} from "react";
import {daySpecToString} from "./UseOpenHours.js";
import Popup from "reactjs-popup";

function Menu({advanceTick, undoAction, players, setActionPlayer, openHours, rules}) {

    const DEFAULT_SELECTED_PLAYER = "";
    const [selectedPlayer, setSelectedPlayer] = useState(DEFAULT_SELECTED_PLAYER);
    const [popupOpen, setPopupOpen] = useState(false);

    let popup = <></>;
    if (popupOpen) {
        popup = <Popup position="bottom center"  open={popupOpen} arrow={false} closeOnDocumentClick={true} onClose={() => setPopupOpen(false)}>
            <div className="rules-popup-overlay" />
            <div className="rules-popup-container">
                <button onClick={() => {
                    setPopupOpen(false);
                }}>Close</button>
                <div className="rules-scroll-box">
                    { rules }
                </div>
            </div>
        </Popup>;
    }

    return (
        <div className="menu-container">
            <h2>Admin Tools</h2>
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
            <h2>Player Tools</h2>
            {rules && <button onClick={() => setPopupOpen(true)}>Rules</button>}
            { popup }
            <span>Councilors</span>
            <select className="player-selector" defaultValue="default"
                    onChange={(event) => setSelectedPlayer(event.target.value)}>
                <option value="default" disabled></option>
                {players.map((player) => (<option key={player} value={player}>{player}</option>))}
            </select>
            <button disabled={selectedPlayer === DEFAULT_SELECTED_PLAYER} onClick={() => {
                if (selectedPlayer !== DEFAULT_SELECTED_PLAYER) {
                    setActionPlayer.current(selectedPlayer);
                }
            }}>Act
            </button>
            {openHours.length === 0 ? <h3>Open every day</h3> : <h3>Open Hours</h3>}
            {openHours.map((date, index) => <span key={index}>{daySpecToString(date)}</span>)}
        </div>
    );
}

export default Menu;
