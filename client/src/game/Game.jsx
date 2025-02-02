import {useParams} from "react-router-dom";
import {fetchGame, fetchState, postTick, postUndoAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useRef, useState} from "react";
import Board from "./board/Board.jsx";
import promptMatches from "../util/prompt.js";
import Logbook from "./Logbook.jsx";
import ActionSelector from "./ActionSelector.jsx";
import Menu from "./Menu.jsx";
import "./Game.css";

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

    const UNLOADED_GAME_NAME = "Loading...";

    const [game, setGame] = useState({
        name: UNLOADED_GAME_NAME,
        logbook: []
    });

    const [positionOptions, setPositionOptions] = useState([]);
    const selectPositionFunction = useRef(() => {});
    const selectPlayerForActionFunction = useRef(() => {});
    const clearActionSelectorFunction = useRef(() => {});

    const updateActiveGame = useCallback((newGame) => {
        if (activeGame === newGame - 1 || activeGame > newGame || game.name === UNLOADED_GAME_NAME) {
            setActiveGame(newGame);
        }
    }, [activeGame]);

    const updateLogbook = useCallback(() => {
        return fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            setGame({...game, logbook: data.logbook, name: data.name});
            updateActiveGame(data.logbook.length);
        });
    }, [game, updateActiveGame]);

    function updateLogbookForever() {
        updateLogbook().then(() => {
            setTimeout(updateLogbookForever, 2000);
        })
    }

    useEffect(() => {
        updateLogbookForever();
    }, []);

    useEffect(() => {
        fetchState(SERVER_URL, uuid, activeGame).then(res => res.json()).then(data => {
            setState(data);
        });
    }, [activeGame]);

    const advanceTick = useCallback(() => {
        if (promptMatches("This action is only intended to be used by the admin\nEnter the magic word", "Abracadabra", "dawn")) {
            postTick(SERVER_URL, uuid).then(updateLogbook).then(clearActionSelectorFunction.current);
        } else {
            alert("Wrong answer");
        }
    }, [updateLogbook]);

    const undoAction = useCallback(() => {
        if (promptMatches("This action is only intended to be used by the admin\nEnter the magic word", "Abracadabra", "nope")) {
            postUndoAction(SERVER_URL, uuid).then(updateLogbook).then(clearActionSelectorFunction.current);
        } else {
            alert("Wrong answer");
        }
    }, [updateLogbook]);

    const gameIsCurrent = game.logbook.length === activeGame;

    return (
        <div>
            <h2>
                {game.name}
            </h2>
            <div className="logbook-board-container">
                <Logbook logbook={game.logbook} activeGame={activeGame} setActiveGame={setActiveGame} />
                <Board board={state.$BOARD} selectMode={positionOptions.length > 0} gameIsCurrent={gameIsCurrent} positionOptions={positionOptions} selectPosition={selectPositionFunction} clearSelectionMode={() => setPositionOptions([])} selectPlayerForActionFunction={selectPlayerForActionFunction} />
                <Menu advanceTick={advanceTick} undoAction={undoAction}  />
            </div>
            <ActionSelector
                enabled={gameIsCurrent}
                uuid={uuid}
                update={updateLogbook}
                setPositionOptions={setPositionOptions}
                selectPositionFunction={selectPositionFunction}
                selectPlayerForActionFunction={selectPlayerForActionFunction}
                clearActionSelectorFunction={clearActionSelectorFunction}
            />
        </div>
    );
}

export default Game;