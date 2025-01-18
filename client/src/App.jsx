import './App.css'
import {HashRouter, Route, Routes} from "react-router-dom";
import Home from "./Home.jsx";
import Game from "./Game.jsx";

function App() {
    return (
        <HashRouter>
            <Routes>
                <Route path="/" element={ <Home /> } />
                <Route path="/game/:uuid" element={ <Game /> } />
            </Routes>
        </HashRouter>
    );
}

export default App
