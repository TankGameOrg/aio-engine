package pro.trevor.tankgame.rule.impl.action.upgrade;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class Upgrade implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        PlayerRef subject = maybeSubject.get();
        Optional<Tank> maybeTank = state.getTankForPlayerRef(subject);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject with a corresponding tank");
        }

        Optional<Boon> maybeBoon = entry.get(Attribute.TARGET_BOON);
        if (maybeBoon.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target boon");
        }

        Tank tank = maybeTank.get();
        Boon boon = maybeBoon.get();

        if (tank.has(Attribute.BOON)) {
            return new Error(Error.Type.OTHER, "Subject tank already has an upgrade");
        }

        tank.put(Attribute.SCRAP, tank.getUnsafe(Attribute.SCRAP) - 6);
        tank.put(Attribute.CAN_ACT, false);
        tank.put(Attribute.BOON, boon);
        boon.apply(tank);

        return Error.NONE;
    }
}
