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

public class OfferSponsorship implements Action {
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

        Optional<PlayerRef> maybeTarget = entry.get(Attribute.TARGET_PLAYER);
        if (maybeTarget.isEmpty()) {
            return new Error(Error.Type.OTHER, "Log entry does not contain a target player");
        }

        PlayerRef target = maybeTarget.get();
        Optional<Tank> maybeTank = state.getTankForPlayerRef(target);
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Target player does not have a tank");
        }

        Tank tank = maybeTank.get();
        if (tank.has(Attribute.SPONSOR)) {
            return new Error(Error.Type.OTHER, "Target tank already has a sponsor");
        }

        // Remove the previous sponsorship offer if applicable
        if (player.has(Attribute.SPONSORED_PLAYER)) {
            Optional<Tank> maybeOldSponsorOfferTank = state.getTankForPlayerRef(player.getUnsafe(Attribute.SPONSORED_PLAYER));
            if (maybeOldSponsorOfferTank.isPresent()) {
                Tank oldSponsorOfferTank = maybeOldSponsorOfferTank.get();
                ListEntity<PlayerRef> oldSponsorOfferTankList = oldSponsorOfferTank.getUnsafe(Attribute.OFFERED_SPONSORS);
                oldSponsorOfferTankList.remove(subject);
                oldSponsorOfferTank.put(Attribute.OFFERED_SPONSORS, oldSponsorOfferTankList);
            }
        }

        ListEntity<PlayerRef> offeredSponsors = tank.getOrElse(Attribute.OFFERED_SPONSORS, new ListEntity<>());
        offeredSponsors.add(subject);
        tank.put(Attribute.OFFERED_SPONSORS, offeredSponsors);
        player.put(Attribute.SPONSORED_PLAYER, target);

        return Error.NONE;
    }
}
