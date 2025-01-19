import {useParams} from "react-router-dom";
import {fetchGame, postTick, postUndoAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useEffect, useState} from "react";
import Board from "./board/Board.jsx";
import entryToText from "../util/logbook.js";
import promptMatches from "../util/prompt.js";

function Game() {
    const uuid = useParams().uuid;
    const [game, setGame] = useState({
        name: "Loading...",
        state: {
            $BOARD: {
                units: [],
                floors: [],
                width: 0,
                height: 0,
            }
        },
        logbook: []
    });

    async function updateGame() {
        return fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            setGame(data);
        });
    }

    function updateGameBoardFromServerForever() {
        updateGame().then(() => {
            setTimeout(updateGameBoardFromServerForever, 5000);
        });
    }

    useEffect(() => {
        updateGameBoardFromServerForever();
    }, []);

    async function advanceTick() {
        if (promptMatches("Enter the magic word", "Abracadabra", "please")) {
            postTick(SERVER_URL, uuid).then(updateGame);
        } else {
            alert("Wrong answer");
        }
    }

    async function undoAction() {
        if (promptMatches("Enter the magic word", "Abracadabra", "nope")) {
            postUndoAction(SERVER_URL, uuid).then(updateGame);
        } else {
            alert("Wrong answer");
        }
    }

    return (
        <div>
            <h1>
                {game.name}
            </h1>
            <ul>
                {(game.logbook).map((entry, index) => <li key={index}> {entryToText(entry)} </li>)}
            </ul>
            <Board board={game.state.$BOARD}/>
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
        </div>
    );
}

export default Game;