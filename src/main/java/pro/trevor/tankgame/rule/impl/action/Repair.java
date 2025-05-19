package pro.trevor.tankgame.rule.impl.action;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.IUnit;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;

import java.util.Optional;

public class Repair implements Action {

    private final int cost;
    private final int regeneration;
    private final int maxRepairDurability;

    public Repair(int cost, int regeneration) {
        this(cost, regeneration, Integer.MAX_VALUE);
    }

    public Repair(int cost, int regeneration, int maxRepairDurability) {
        this.cost = cost;
        this.regeneration = regeneration;
        this.maxRepairDurability = maxRepairDurability;
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

        if (tank.getOrElse(Attribute.SCRAP, 0) < cost) {
            return new Error(Error.Type.OTHER, "Subject tank does not have enough scrap");
        }

        Position position = maybePosition.get();

        Optional<IUnit> maybeUnit = state.getBoard().getUnit(position);
        if (maybeUnit.isEmpty()) {
            return new Error(Error.Type.OTHER, "Target position is not on the game board");
        }

        IUnit unit = maybeUnit.get();
        AttributeEntity entity = (AttributeEntity) unit;

        if (!entity.has(Attribute.DURABILITY)) {
            return new Error(Error.Type.OTHER, "Target unit does not have a durability");
        }

        tank.put(Attribute.SCRAP, tank.getUnsafe(Attribute.SCRAP) - cost);
        entity.put(Attribute.DURABILITY, Math.min(maxRepairDurability, entity.getUnsafe(Attribute.DURABILITY) + regeneration));

        return Error.NONE;
    }

    public int getMaxRepairDurability() {
        return maxRepairDurability;
    }
}
