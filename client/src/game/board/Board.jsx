import BoardCell from "./BoardCell.jsx";
import BoardOuterCell from "./BoardOuterCell.jsx";
import {stringToPosition} from "../../util/position.js";

function arrayContainsPosition(array, position) {
    if (array === undefined || array === null || array.length === 0) {
        return false;
    }

    for (let i = 0; i < array.length; ++i) {
        let element = array[i];

        if (typeof element == "string" || element instanceof String) {
            element = stringToPosition(element);
        }

        if (element.x === position.x && element.y === position.y) {
            return true;
        }
    }
    return false;
}

function Board({board, selectMode, gameIsCurrent, selectPosition, positionOptions, clearSelectionMode, selectPlayerForActionFunction}) {
    const { units, floors, width, height } = board;

    if (width === 0 || height === 0) {
        return (<span>Loading...</span>);
    }

    const unitBoard = Array.from({length: height},
        () => Array.from({length: width}, () => ({}))
    );

    const floorBoard = Array.from({length: height},
        () => Array.from({length: width}, () => ({}))
    );

    for (let unit of units) {
        const { x, y } = unit.$POSITION;
        unitBoard[y][x] = unit;
    }

    for (let floor of floors) {
        const { x, y } = floor.$POSITION;
        floorBoard[y][x] = floor;
    }

    return (
        <div className="board-wrapper">
            <div className="board-row">
                { Array(width + 1).fill(0).map((_, index) => <BoardOuterCell number={index} isLetter={true} key={index} />) }
            </div>
            { unitBoard.map((row, rowIndex) =>
                <div className="board-row" key={rowIndex}>
                    <BoardOuterCell number={rowIndex + 1} isLetter={false} />
                    { row.map((element, columnIndex) =>
                        <BoardCell
                            key={`${rowIndex}:${columnIndex}`}
                            unit={element}
                            floor={floorBoard[rowIndex][columnIndex]}
                            onClick={() => {
                                selectPosition.current(columnIndex, rowIndex);
                                clearSelectionMode();
                            }}
                            gameIsCurrent={gameIsCurrent}
                            selectMode={selectMode}
                            enabled={selectMode && arrayContainsPosition(positionOptions, {x: columnIndex, y: rowIndex})}
                            selectPlayer={selectPlayerForActionFunction}
                        />
                    )}
                </div>
            )}
        </div>
    );
}

export default Board;
