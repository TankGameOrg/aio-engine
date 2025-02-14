package pro.trevor.tankgame.rule.impl.apply;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.apply.Apply;
import pro.trevor.tankgame.rule.handle.Damage;
import pro.trevor.tankgame.rule.handle.Destroy;
import pro.trevor.tankgame.rule.handle.cause.PseudoTankCause;
import pro.trevor.tankgame.rule.impl.action.fallen.ActionType;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.IUnit;
import pro.trevor.tankgame.state.board.unit.EmptyUnit;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.util.IRandom;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.util.Random;

import java.util.Optional;

public class ApplyRandomActionOption implements Apply {

    private final Ruleset ruleset;

    public ApplyRandomActionOption(Ruleset ruleset) {
        this.ruleset = ruleset;
    }

    @Override
    public Optional<LogEntry> apply(State state) {
        Optional<PseudoTank> maybeTank = state.getBoard().gather(PseudoTank.class).findAny();
        if (maybeTank.isEmpty()) {
            // Do nothing
            return Optional.empty();
        }
        PseudoTank pseudoTank = maybeTank.get();
        ListEntity<LogEntry> options = pseudoTank.getOrElse(Attribute.ACTION_OPTIONS, new ListEntity<>());
        if (options.isEmpty()) {
            return Optional.empty();
        }

        IRandom random = state.getOrElse(Attribute.RANDOM, new Random(System.currentTimeMillis()));
        int selectionNumber = random.nextInt(options.size());
        state.put(Attribute.RANDOM, random);

        LogEntry selection = options.get(selectionNumber);
        if (!selection.has(Attribute.FALLEN_ACTION)) {
            return Optional.empty();
        }

        ActionType actionType = selection.getUnsafe(Attribute.FALLEN_ACTION);
        switch (actionType) {
            case SHOOT -> shoot(state, selection, pseudoTank);
            case MOVE -> move(state, selection, pseudoTank);
            case REMAIN -> { /* Intentionally left blank */ }
        }

        pseudoTank.put(Attribute.ACTION_OPTIONS, new ListEntity<>());
        return Optional.of(selection);
    }

    private void shoot(State state, LogEntry entry, PseudoTank pseudoTank) {
        Optional<Position> maybePosition = entry.get(Attribute.TARGET_POSITION);
        if (maybePosition.isEmpty()) {
            return;
        }

        Optional<IUnit> maybeUnit = state.getBoard().getUnit(maybePosition.get());
        if (maybeUnit.isEmpty()) {
            return;
        }

        IUnit unit = maybeUnit.get();
        AttributeEntity entity = (AttributeEntity) unit;

        for (Damage damageHandler : ruleset.getDamageHandlers()) {
            if (damageHandler.getPredicate().test(entity)) {
                damageHandler.getHandle().damage(state, new PseudoTankCause(pseudoTank), entity, entry);
                break;
            }
        }

        if (entity.get(Attribute.DURABILITY).map((durability) -> durability <= 0).orElse(false)) {
            for (Destroy destroyHandler : ruleset.getDestroyHandlers()) {
                if (destroyHandler.getPredicate().test(entity)) {
                    destroyHandler.getHandle().destroy(state, new PseudoTankCause(pseudoTank), entity, entry);
                }
            }
        }
    }

    private void move(State state, LogEntry entry, PseudoTank pseudoTank) {
        Position oldPosition = pseudoTank.getPosition();
        Position newPosition = entry.getOrElse(Attribute.TARGET_POSITION, oldPosition);

        pseudoTank.setPosition(newPosition);
        state.getBoard().putUnit(new EmptyUnit(oldPosition));
        state.getBoard().putUnit(pseudoTank);
    }
}
