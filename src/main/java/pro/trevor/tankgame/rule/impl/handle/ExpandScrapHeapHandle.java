package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.DestroyHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.floor.ScrapHeap;
import pro.trevor.tankgame.util.MathUtil;
import pro.trevor.tankgame.util.Position;

public class ExpandScrapHeapHandle implements DestroyHandle {
    @Override
    public void destroy(State state, Cause cause, AttributeEntity target, LogEntry entry) {
        Position position = target.getUnsafe(Attribute.POSITION);
        if (MathUtil.isOrthAdjToMine(state, position)) {
            state.getBoard().putFloor(new ScrapHeap(position));
        }
    }
}
