package pro.trevor.tankgame.rule.impl.action;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;
import pro.trevor.tankgame.rule.handle.cause.TankCause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.IUnit;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;

import java.util.Optional;

public class Shoot implements Action {

    private final Ruleset ruleset;

    public Shoot(Ruleset ruleset) {
        this.ruleset = ruleset;
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

        Optional<Position> maybePosition = entry.get(Attribute.TARGET_POSITION);
        if (maybePosition.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target position");
        }

        Tank tank = maybeTank.get();
        Position position = maybePosition.get();

        Optional<IUnit> maybeUnit = state.getBoard().getUnit(position);
        if (maybeUnit.isEmpty()) {
            return new Error(Error.Type.OTHER, "Target position is not on the game board");
        }

        IUnit unit = maybeUnit.get();
        AttributeEntity entity = (AttributeEntity) unit;

        for (Damage damageHandler : ruleset.getDamageHandlers()) {
            if (damageHandler.getPredicate().test(entity)) {
                damageHandler.getHandle().damage(state, new TankCause(tank), entity, entry);
                break;
            }
        }

        if (entity.get(Attribute.DURABILITY).map((durability) -> durability <= 0).orElse(false)) {
            for (Destroy destroyHandler : ruleset.getDestroyHandlers()) {
                if (destroyHandler.getPredicate().test(entity)) {
                    destroyHandler.getHandle().destroy(state, new TankCause(tank), entity, entry);
                }
            }
        }

        tank.put(Attribute.CAN_ACT, false);

        return Error.NONE;
    }
}
