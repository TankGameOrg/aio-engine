package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.DestroyHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.EmptyUnit;

public class DestroyEntityHandle implements DestroyHandle {
    @Override
    public void destroy(State state, Cause cause, AttributeEntity target, LogEntry entry) {
        state.getBoard().putUnit(new EmptyUnit(target.getUnsafe(Attribute.POSITION)));
    }
}
