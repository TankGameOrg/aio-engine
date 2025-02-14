package pro.trevor.tankgame.rule.impl.action.fallen;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class RemainFallen implements Action {

    @Override
    public pro.trevor.tankgame.rule.action.Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new pro.trevor.tankgame.rule.action.Error(pro.trevor.tankgame.rule.action.Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        PlayerRef subject = maybeSubject.get();
        if (state.getTankForPlayerRef(subject).isPresent()) {
            return new pro.trevor.tankgame.rule.action.Error(pro.trevor.tankgame.rule.action.Error.Type.OTHER, "Subject is not a councilor");
        }

        Optional<Player> maybePlayer = subject.toPlayer(state);
        if (maybePlayer.isEmpty()) {
            return new Error(Error.Type.OTHER, "Subject is not a player");
        }

        Player player = maybePlayer.get();
        if (player.getOrElse(Attribute.HAS_COMPELLED_FALLEN, false)) {
            return new Error(Error.Type.OTHER, "Subject has already compelled the fallen");
        }

        Optional<PseudoTank> maybeTank = state.getBoard().gather(PseudoTank.class).findFirst();
        if (maybeTank.isEmpty()) {
            return new pro.trevor.tankgame.rule.action.Error(pro.trevor.tankgame.rule.action.Error.Type.OTHER, "There is no fallen tank to move");
        }

        PseudoTank tank = maybeTank.get();

        LogEntry newEntry = new LogEntry();
        newEntry.put(Attribute.FALLEN_ACTION, ActionType.REMAIN);

        player.put(Attribute.HAS_COMPELLED_FALLEN, true);
        ListEntity<LogEntry> actions = tank.getOrElse(Attribute.ACTION_OPTIONS, new ListEntity<>());
        actions.add(newEntry);
        tank.put(Attribute.ACTION_OPTIONS, actions);

        return Error.NONE;
    }

}
