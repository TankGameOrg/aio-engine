package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Condition;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class PlayerTankCanActCondition implements Condition {
    @Override
    public Error test(State state, LogEntry entry) {
        PlayerRef playerRef = entry.getUnsafe(Attribute.SUBJECT);
        Optional<Tank> optionalTank = state.getTankForPlayerRef(playerRef);
        if (optionalTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Player has no tank");
        }

        Tank tank = optionalTank.get();
        if (tank.getOrElse(Attribute.CAN_ACT, false)) {
            return Error.NONE;
        } else {
            return new Error(Error.Type.OTHER, "Player tank cannot act");
        }
    }
}
