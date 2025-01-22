import {useParams} from "react-router-dom";
import {fetchGame, fetchState, postTick, postUndoAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useRef, useState} from "react";
import Board from "./board/Board.jsx";
import promptMatches from "../util/prompt.js";
import Logbook from "./Logbook.jsx";

function Game() {
    const uuid = useParams().uuid;

    const [activeGame, setActiveGame] = useState(0);
    const [state, setState] = useState(
        { $BOARD: {
            units: [],
            floors: [],
            width: 0,
            height: 0,
        }}
    );
    const mostRecentGame = useRef(0);
    const game = useRef({
        name: "Loading...",
        logbook: []
    });

    const updateLogbook = useCallback(() => {
        return fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            game.current.logbook = data.logbook;
            game.current.name = data.name;
            const number = data.logbook.length;
            const doSwitch = (mostRecentGame.current === activeGame);
            mostRecentGame.current = number;
            if (doSwitch) {
                setActiveGame(number);
            }
        });
    }, [activeGame]);

    function updateLogbookFromServerForever() {
        updateLogbook().then(() => {
            setTimeout(updateLogbookFromServerForever, 5000);
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
            postTick(SERVER_URL, uuid).then(updateLogbook).then(() => setActiveGame(mostRecentGame.current));
        } else {
            alert("Wrong answer");
        }
    }, [updateLogbook]);

    const undoAction = useCallback(() => {
        if (promptMatches("Enter the magic word", "Abracadabra", "nope")) {
            postUndoAction(SERVER_URL, uuid).then(updateLogbook).then(() => setActiveGame(mostRecentGame.current));
        } else {
            alert("Wrong answer");
        }
    }, [updateLogbook]);

    console.log(game.current);

    return (
        <div>
            <h1>
                {game.current.name}
            </h1>
            <Logbook logbook={game.current?.logbook} activeGame={activeGame} setActiveGame={setActiveGame} />
            <Board board={state.$BOARD}/>
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
        </div>
    );
}

export default Game;