import {useEffect, useState} from "react";
import {fetchGames} from "./util/fetch.js";
import {SERVER_URL} from "./util/constants.js";
import GameButton from "./GameButton.jsx";

function Home() {
    const [gamesList, setGamesList] = useState([]);

    function updateGamesList() {
        fetchGames(SERVER_URL).then(res => res.json()).then(data => {
            setGamesList(data);
            setTimeout(updateGamesList, 30000);
        });
    }

    useEffect(() => {
        updateGamesList();
    }, []);

    return (
        <div>
            <h1>Tank Game Redux</h1>
            { gamesList.map(game => <GameButton game={game} key={game.uuid} />) }
        </div>
    )
}

export default Home;
