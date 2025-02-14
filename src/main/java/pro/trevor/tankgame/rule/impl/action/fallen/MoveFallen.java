package pro.trevor.tankgame.rule.impl.action.fallen;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.IUnit;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;

import java.util.Optional;

public class MoveFallen implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        PlayerRef subject = maybeSubject.get();
        if (state.getTankForPlayerRef(subject).isPresent()) {
            return new Error(Error.Type.OTHER, "Subject is not a councilor");
        }

        Optional<Player> maybePlayer = subject.toPlayer(state);
        if (maybePlayer.isEmpty()) {
            return new Error(Error.Type.OTHER, "Subject is not a player");
        }

        Player player = maybePlayer.get();
        if (player.getOrElse(Attribute.HAS_COMPELLED_FALLEN, false)) {
            return new Error(Error.Type.OTHER, "Subject has already compelled the fallen");
        }

        Optional<Position> maybePosition = entry.get(Attribute.TARGET_POSITION);
        if (maybePosition.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target position");
        }

        Optional<PseudoTank> maybeTank = state.getBoard().gather(PseudoTank.class).findFirst();
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "There is no fallen tank to move");
        }

        PseudoTank tank = maybeTank.get();
        Position position = maybePosition.get();

        Optional<IUnit> maybeUnit = state.getBoard().getUnit(position);
        if (maybeUnit.isEmpty()) {
            return new Error(Error.Type.OTHER, "Target position is not on the game board");
        }

        LogEntry newEntry = new LogEntry();
        newEntry.put(Attribute.TARGET_POSITION, position);
        newEntry.put(Attribute.FALLEN_ACTION, ActionType.MOVE);

        player.put(Attribute.HAS_COMPELLED_FALLEN, true);
        ListEntity<LogEntry> actions = tank.getOrElse(Attribute.ACTION_OPTIONS, new ListEntity<>());
        actions.add(newEntry);
        tank.put(Attribute.ACTION_OPTIONS, actions);

        return Error.NONE;
    }
}
