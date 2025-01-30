package pro.trevor.tankgame.rule.impl.action;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;

public class ExampleAction implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        PlayerRef playerRef = entry.getUnsafe(Attribute.SUBJECT);
        Tank tank = state.getTankForPlayerRef(playerRef).get();
        tank.put(Attribute.GOLD, tank.getOrElse(Attribute.GOLD, 0) + entry.getOrElse(Attribute.GOLD, 0));
        return Error.NONE;
    }
}
