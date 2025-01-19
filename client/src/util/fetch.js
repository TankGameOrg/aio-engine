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

function postAction(url, uuid, entry) {
    return fetch(`${url}/game/${uuid}/action`,
        {
            method: 'POST',
            body: JSON.stringify(entry),
        });
}

function postUndoAction(url, uuid) {
    return fetch(`${url}/game/${uuid}/undo`,
        {
            method: 'POST',
        }
    );
}

function postTick(url, uuid) {
    return fetch(`${url}/game/${uuid}/tick`,
        {
            method: 'POST',
        }
    );
}

export { fetchGames, fetchGame, fetchState, postAction, postUndoAction, postTick };
