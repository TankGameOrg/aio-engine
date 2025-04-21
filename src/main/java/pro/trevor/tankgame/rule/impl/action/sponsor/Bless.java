package pro.trevor.tankgame.rule.impl.action.sponsor;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class Bless implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        PlayerRef subject = maybeSubject.get();
        Optional<Player> maybePlayer = subject.toPlayer(state);
        if (maybePlayer.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject with a player");
        }

        Player player = maybePlayer.get();
        Optional<PlayerRef> maybeTarget = player.get(Attribute.SPONSORED_PLAYER);
        if (maybeTarget.isEmpty()) {
            return new Error(Error.Type.OTHER, "Subject does not have a sponsored player");
        }

        if (!player.getOrElse(Attribute.CAN_BLESS, true)) {
            return new Error(Error.Type.OTHER, "Subject has already performed the bless action");
        }

        PlayerRef target = maybeTarget.get();
        Optional<Tank> maybeTank = state.getTankForPlayerRef(target);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Sponsored player does not have a tank");
        }

        Tank tank = maybeTank.get();
        if (tank.get(Attribute.SPONSOR).map((sponsor) -> !sponsor.equals(subject)).orElse(false)) {
            return new Error(Error.Type.OTHER, "Sponsored tank does not have subject as its sponsor");
        }

        if (tank.getOrElse(Attribute.CAN_ACT, false)) {
            return new Error(Error.Type.OTHER, "Sponsored tank has not yet acted today");
        }

        entry.put(Attribute.TARGET_PLAYER, target);
        player.put(Attribute.CAN_BLESS, false);
        tank.put(Attribute.CAN_ACT, true);

        return Error.NONE;
    }
}
