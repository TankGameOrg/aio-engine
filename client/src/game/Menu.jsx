import "./Menu.css"

function Menu({advanceTick, undoAction}) {
    return (
        <div className="menu-container">
            <button onClick={() => advanceTick()}>Next Day</button>
            <button onClick={() => undoAction()}>Undo Previous Action</button>
        </div>
    );
}

export default Menu;
