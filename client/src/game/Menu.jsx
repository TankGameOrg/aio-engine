import "./Menu.css"
import {useState} from "react";

function Menu({advanceTick, undoAction, players, setActionPlayer}) {

    const DEFAULT_SELECTED_PLAYER = "";
    const [selectedPlayer, setSelectedPlayer] = useState(DEFAULT_SELECTED_PLAYER);

    return (
        <div className="menu-container">
            <span>Admin Tools</span>
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
            <span>Councilors</span>
            <select className="player-selector" defaultValue="default" onChange={(event) => setSelectedPlayer(event.target.value)}>
                <option value="default" disabled></option>
                { players.map((player) => (<option key={player} value={player}>{player}</option>)) }
            </select>
            <button disabled={selectedPlayer === DEFAULT_SELECTED_PLAYER} onClick={() => {
                if (selectedPlayer !== DEFAULT_SELECTED_PLAYER) {
                    setActionPlayer.current(selectedPlayer);
                }
            }}>Act</button>
        </div>
    );
}

export default Menu;
