
function GameButton({game}) {
    return (
        <div>
            <button onClick={() => {
                window.location = `#/game/${game.uuid}`
            }}>
                { game.name } | { game.ruleset }
            </button>
        </div>
    );
}

export default GameButton;

