import {useParams} from "react-router-dom";
import {fetchGame} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useEffect, useState} from "react";
import Board from "./board/Board.jsx";

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

    function updateGameBoard() {
        fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            setGame(data);
            setTimeout(updateGameBoard, 5000);
        });
    }

    useEffect(() => {
        updateGameBoard();
    }, []);

    return (
        <div>
            <h1>
                { game.name }
            </h1>
            <ul>
                { (game.logbook).map((entry, index) => <li key={index}> { entry.subject.name } takes action { entry.action } </li>) }
            </ul>
            <Board board={game.state.$BOARD} />
        </div>
    );
}

export default Game;