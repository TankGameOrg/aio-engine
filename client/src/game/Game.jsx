import {useParams} from "react-router-dom";
import {fetchGame, fetchRules, fetchState, postTick, postUndoAction} from "../util/fetch.js";
import {SERVER_URL} from "../util/constants.js";
import {useCallback, useEffect, useRef, useState} from "react";
import Board from "./board/Board.jsx";
import promptMatches from "../util/prompt.js";
import Logbook from "./Logbook.jsx";
import ActionSelector from "./ActionSelector.jsx";
import Menu from "./Menu.jsx";
import useOpenHours, {daySpecToString} from "./UseOpenHours.js";
import Markdown from "marked-react";
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

    const running = state?.$RUNNING ?? true;
    const winners = state?.$WINNER ?? "";

    const UNLOADED_GAME_NAME = "Loading...";

    const [game, setGame] = useState({
        name: UNLOADED_GAME_NAME,
        logbook: []
    });
    const [rules, setRules] = useState(null);
    const loading = useRef(true);

    const [positionOptions, setPositionOptions] = useState([]);
    const selectPositionFunction = useRef(() => {});
    const selectPlayerForActionFunction = useRef(() => {});
    const clearActionSelectorFunction = useRef(() => {});
    const scrollToActiveGameFunction = useRef(() => {});

    const updateActiveGame = useCallback((newGame) => {
        if (activeGame === newGame - 1 || activeGame > newGame || loading.current) {
            setActiveGame(newGame);
            scrollToActiveGameFunction.current(newGame);
        }
    }, [activeGame]);

    const updateLogbook = useCallback(() => {
        return fetchGame(SERVER_URL, uuid).then(res => res.json()).then(data => {
            setGame({...game, logbook: data.logbook, name: data.name});
            updateActiveGame(data.logbook.length);
            loading.current = false;
        });
    }, [game, updateActiveGame]);

    function updateLogbookForever() {
        updateLogbook().then(() => {
            setTimeout(updateLogbookForever, 2000);
        })
    }

    useEffect(() => {
        updateLogbookForever();
        fetchRules(SERVER_URL, uuid).then(res => res.json()).then(data => setRules(<Markdown value={data?.rules} />));
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

    const [isOpen, willOpen, timeUntilOpen, openHours] = useOpenHours(uuid);
    openHours.sort((a, b) => a.day - b.day);
    if (!isOpen) {
        return (
            <div>
                <h1>This game is currently closed</h1>
                { willOpen &&
                    <h2>The game will open in {timeUntilOpen} {timeUntilOpen === 1 ? "minute" : "minutes"}</h2>
                }
                <h2>Open Hours</h2>
                { openHours.map((date) => <p>{daySpecToString(date)}</p>) }
            </div>
        );
    }

    const gameIsCurrent = game.logbook.length === activeGame;
    const allPlayers = state.$PLAYERS.elements.map((player) => player.$NAME).sort((a, b) => a.localeCompare(b));
    const livingPlayers = state.$BOARD.units.filter((unit) => unit.$PLAYER_REF !== undefined).map((unit) => unit.$PLAYER_REF.name);
    const councilPlayers = allPlayers.filter((player) => livingPlayers.indexOf(player) < 0);

    return (
        <div>
            <h2>
                {game.name}
            </h2>
            <div className="logbook-board-container">
                <Logbook logbook={game.logbook} activeGame={activeGame} setActiveGame={setActiveGame} scrollToActiveGameFunction={scrollToActiveGameFunction} />
                <Board board={state.$BOARD} selectMode={positionOptions.length > 0} gameIsCurrent={gameIsCurrent} positionOptions={positionOptions} selectPosition={selectPositionFunction} clearSelectionMode={() => setPositionOptions([])} selectPlayerForActionFunction={selectPlayerForActionFunction} />
                <Menu advanceTick={advanceTick} undoAction={undoAction} players={councilPlayers} setActionPlayer={selectPlayerForActionFunction} openHours={openHours} rules={rules} />
            </div>
            { running ?
                <ActionSelector
                    enabled={gameIsCurrent}
                    uuid={uuid}
                    update={updateLogbook}
                    setPositionOptions={setPositionOptions}
                    selectPositionFunction={selectPositionFunction}
                    selectPlayerForActionFunction={selectPlayerForActionFunction}
                    clearActionSelectorFunction={clearActionSelectorFunction}
                />
                :
                <>
                    <h2>The game is over!</h2>
                    <span>
                        { winners === "" ? "There are no winners" : `Winners: ${winners}` }
                    </span>
                </>
            }
        </div>
    );
}

export default Game;