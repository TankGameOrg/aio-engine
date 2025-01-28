package pro.trevor.tankgame.rule.impl.action;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.EmptyUnit;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;

import java.util.Optional;

public class ExampleMove implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        PlayerRef subject = entry.getUnsafe(Attribute.SUBJECT);

        Optional<Position> maybePosition = entry.get(Attribute.TARGET_POSITION);
        if (maybePosition.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a position");
        }

        Optional<Tank> maybeTank = state.getTankForPlayerRef(subject);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject with a corresponding tank");
        }

        Position position = maybePosition.get();
        Tank tank = maybeTank.get();

        Position oldPosition = tank.getPosition();

        tank.setPosition(position);
        state.getBoard().putUnit(tank);
        state.getBoard().putUnit(new EmptyUnit(oldPosition));

        return Error.NONE;
    }
}
