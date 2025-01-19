import "./BoardCell.css"

function BoardOuterCell({number, isLetter}) {

    let text;
    if (isLetter) {
        if (number === 0) {
            text = "";
        } else {
            text = String.fromCharCode(64 + number);
        }
    } else {
        text = `${number}`;
    }

    return (
        <div className={`board-cell board-cell-background-dark-gray`}>
            <span className="board-outer-cell-content">{text}</span>
        </div>
    );
}

export default BoardOuterCell;
