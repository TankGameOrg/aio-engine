import {useParams} from "react-router-dom";
import {fetchGame, fetchState, postTick, postUndoAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useRef, useState} from "react";
import Board from "./board/Board.jsx";
import promptMatches from "../util/prompt.js";
import Logbook from "./Logbook.jsx";
import "./Game.css";
import ActionSelector from "./ActionSelector.jsx";

function Game() {
    const uuid = useParams().uuid;

    const [activeGame, setActiveGame] = useState(0);
    const [state, setState] = useState(
        {
            $BOARD: {
                units: [],
                floors: [],
                width: 0,
                height: 0,
            }, $PLAYERS: {
                elements: [],
            }
        }
    );
    const [game, setGame] = useState({
        name: "Loading...",
        logbook: []
    });

    const [positionOptions, setPositionOptions] = useState([]);
    const selectPositionFunction = useRef(() => {});

    const updateLogbook = useCallback(() => {
        return fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            const previousLogbookLength = game.logbook.length;
            setGame({...game, logbook: data.logbook, name: data.name});
            if (activeGame === previousLogbookLength) {
                setActiveGame(data.logbook.length);
            }
        });
    }, [activeGame, game]);

    function updateLogbookFromServerForever() {
        updateLogbook().then(() => {
            setTimeout(updateLogbookFromServerForever, 2500);
        });
    }

    useEffect(() => {
        updateLogbookFromServerForever();
    }, []);

    useEffect(() => {
        fetchState(SERVER_URL, uuid, activeGame).then(res => res.json()).then(data => {
            setState(data);
        });
    }, [activeGame]);

    const advanceTick = useCallback(() => {
        if (promptMatches("Enter the magic word", "Abracadabra", "dawn")) {
            postTick(SERVER_URL, uuid).then(updateLogbook);
        } else {
            alert("Wrong answer");
        }
    }, [game, updateLogbook]);

    const undoAction = useCallback(() => {
        if (promptMatches("Enter the magic word", "Abracadabra", "nope")) {
            postUndoAction(SERVER_URL, uuid).then(updateLogbook);
        } else {
            alert("Wrong answer");
        }
    }, [game, updateLogbook]);

    return (
        <div>
            <h1>
                {game.name}
            </h1>
            <div className="logbook-board-container">
                <Logbook logbook={game.logbook} activeGame={activeGame} setActiveGame={setActiveGame} />
                <Board board={state.$BOARD} selectMode={positionOptions.length > 0} positionOptions={positionOptions} selectPosition={selectPositionFunction} clearSelectionMode={() => setPositionOptions([])} />
            </div>
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
            <ActionSelector
                enabled={activeGame === game.logbook.length}
                uuid={uuid}
                players={state.$PLAYERS.elements}
                update={updateLogbook}
                setPositionOptions={setPositionOptions}
                selectPositionFunction={selectPositionFunction}
            />
        </div>
    );
}

export default Game;