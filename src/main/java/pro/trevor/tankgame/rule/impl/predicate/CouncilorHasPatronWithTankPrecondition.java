package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.Precondition;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.Optional;

public class CouncilorHasPatronWithTankPrecondition implements Precondition {
    @Override
    public Error test(State state, Player player) {
        if (!player.getOrElse(Attribute.IS_SPONSOR, false)) {
            return new Error(Error.Type.PRECONDITION, "Player is not a sponsor");
        }

        Optional<PlayerRef> maybePatron = player.get(Attribute.SPONSORED_PLAYER);
        if (maybePatron.isEmpty()) {
            return new Error(Error.Type.PRECONDITION, "Player does not have a patron");
        }

        Optional<Tank> tank = state.getTankForPlayerRef(maybePatron.get());
        if (tank.isEmpty()) {
            return new Error(Error.Type.OTHER, "Sponsored player does not have a tank");
        }

        return Error.NONE;
    }
}
