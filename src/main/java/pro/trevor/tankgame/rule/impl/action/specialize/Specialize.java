package pro.trevor.tankgame.rule.impl.action.specialize;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class Specialize implements Action {
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

        Optional<Specialty> maybeSpecialty = entry.get(Attribute.TARGET_SPECIALTY);
        if (maybeSpecialty.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target specialty");
        }

        Tank tank = maybeTank.get();
        Specialty specialty = maybeSpecialty.get();

        if (tank.has(Attribute.SPECIALTY)) {
            return new Error(Error.Type.OTHER, "Target tank already has a specialty");
        }

        tank.put(Attribute.SPECIALTY, specialty);
        specialty.apply(tank);

        return Error.NONE;
    }
}
