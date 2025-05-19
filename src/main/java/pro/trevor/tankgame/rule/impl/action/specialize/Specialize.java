package pro.trevor.tankgame.rule.impl.action.specialize;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Specialize implements Action {

    private final int cost;
    private final List<Specialty> availableSpecialties;

    public Specialize(int cost, List<Specialty> availableSpecialties) {
        this.cost = cost;
        this.availableSpecialties = new ArrayList<>(availableSpecialties);
    }

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

        if (tank.getOrElse(Attribute.SCRAP, 0) < cost) {
            return new Error(Error.Type.OTHER, "Subject tank has insufficient scrap");
        }

        if (!availableSpecialties.contains(specialty)) {
            return new Error(Error.Type.OTHER, "Target specialty does not exist");
        }

        if (tank.has(Attribute.SPECIALTY)) {
            Specialty oldSpecialty = tank.getUnsafe(Attribute.SPECIALTY);
            oldSpecialty.remove(tank);
        }

        tank.put(Attribute.CAN_ACT, false);
        tank.put(Attribute.SCRAP, tank.getUnsafe(Attribute.SCRAP) - cost);
        tank.put(Attribute.SPECIALTY, specialty);
        specialty.apply(tank);

        return Error.NONE;
    }
}
