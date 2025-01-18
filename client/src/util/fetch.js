import * as url from "node:url";

function fetchGames(url) {
    return fetch(`${url}/games/list`, {});
}

function fetchGame(url, uuid) {
    return fetch(`${url}/game/${uuid}`, {});
}

function fetchState(url, uuid, index) {
    return fetch(`${url}/game/${uuid}/${index}`, {});
}

function postAction(url, entry) {
    return fetch(`${url}/game/${uuid}/action`,
        {
            method: 'POST',
            body: JSON.stringify(entry),
        });
}

function postTick(url) {
    return fetch(`${url}/game/${uuid}/tick`,
        {
            method: 'POST',
        }
    );
}

export { fetchGames, fetchGame, fetchState, postAction, postTick };
