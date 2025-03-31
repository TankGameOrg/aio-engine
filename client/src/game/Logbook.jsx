import "./Logbook.css"
import entryToText from "../util/logbook.jsx";
import {useRef} from "react";

function Logbook({logbook, activeGame, setActiveGame, scrollToActiveGameFunction}) {

    const currentAction = useRef([]);

    scrollToActiveGameFunction.current = (index) => setTimeout(() => currentAction.current[index]?.scrollIntoView(), 200);
    
    return (
        <div className="logbook-wrapper">
            <div className="logbook">
                <h2>Logbook</h2>
                <ul className="logbook-list">
                    <li key={0} onClick={() => setActiveGame(0)}>
                        <a className={`logbook-entry ${activeGame === 0 ? "logbook-entry-current" : ""}`}>Initial State</a>
                    </li>
                    {logbook.map((entry, index) => <li key={index + 1} ref={(element) => currentAction.current[index] = element} onClick={() => setActiveGame(index + 1)}>
                        <a className={`logbook-entry ${index + 1 === activeGame ? "logbook-entry-current" : ""}`}>{entryToText(entry)}</a>
                    </li>)}
                </ul>
            </div>
        </div>
    );
}

export default Logbook;
