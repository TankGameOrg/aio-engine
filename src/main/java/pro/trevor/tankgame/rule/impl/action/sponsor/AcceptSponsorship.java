package pro.trevor.tankgame.rule.impl.action.sponsor;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.action.Action;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class AcceptSponsorship implements Action {
    @Override
    public Error apply(State state, LogEntry entry) {
        Optional<PlayerRef> maybeSubject = entry.get(Attribute.SUBJECT);
        if (maybeSubject.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a subject player");
        }

        Optional<PlayerRef> maybeTarget = entry.get(Attribute.TARGET_PLAYER);
        if (maybeTarget.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target player");
        }

        PlayerRef subject = maybeSubject.get();
        PlayerRef target = maybeTarget.get();

        Optional<Player> maybeTargetPlayer = state.getPlayer(target);
        if (maybeTargetPlayer.isEmpty()) {
            return new Error(Error.Type.OTHER, "Target player was not found");
        }

        Player targetPlayer = maybeTargetPlayer.get();

        Optional<Tank> maybeTank = state.getTankForPlayerRef(subject);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Subject player does not have a tank");
        }

        Tank tank = maybeTank.get();
        if (tank.has(Attribute.SPONSOR)) {
            return new Error(Error.Type.OTHER, "Subject tank already has a sponsor");
        }

        ListEntity<PlayerRef> sponsorOffers = tank.getOrElse(Attribute.OFFERED_SPONSORS, new ListEntity<>());
        if (!sponsorOffers.contains(target)) {
            return new Error(Error.Type.OTHER, "Target player has not offered sponsorship");
        }

        tank.remove(Attribute.OFFERED_SPONSORS);
        tank.put(Attribute.SPONSOR, target);
        targetPlayer.put(Attribute.SPONSORED_PLAYER, subject);
        targetPlayer.put(Attribute.IS_SPONSOR, true);

        return Error.NONE;
    }
}
