package pro.trevor.tankgame.rule.handle;

import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.state.State;

public interface DestroyHandle {

    void destroy(State state, Cause cause, AttributeEntity target);

}
