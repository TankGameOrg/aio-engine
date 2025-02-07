package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.List;

public class OfferSponsorshipSupplier implements AvailableParameterSupplier<PlayerRef> {
    @Override
    public ParameterBound<PlayerRef> possibleParameters(State state, Player player) {
        List<PlayerRef> players = state.getBoard().gather(Tank.class).filter((tank) -> !tank.has(Attribute.SPONSOR)).map(Tank::getPlayerRef).toList();
        return new DiscreteValueBound<>(Attribute.TARGET_PLAYER, players);
    }
}
