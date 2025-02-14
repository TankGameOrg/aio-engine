import "./Logbook.css"
import entryToText from "../util/logbook.jsx";

function Logbook({logbook, activeGame, setActiveGame}) {

    return (
        <div className="logbook-wrapper">
            <div className="logbook">
                <h2>Logbook</h2>
                <ul className="logbook-list">
                    <li key={0} onClick={() => setActiveGame(0)}>
                        <a className={`logbook-entry ${activeGame === 0 ? "logbook-entry-current" : ""}`}>Initial State</a>
                    </li>
                    {logbook.map((entry, index) => <li key={index + 1} onClick={() => setActiveGame(index + 1)}>
                        <a className={`logbook-entry ${index + 1 === activeGame ? "logbook-entry-current" : ""}`}>{entryToText(entry)}</a>
                    </li>)}
                </ul>
            </div>
        </div>
    );
}

export default Logbook;
