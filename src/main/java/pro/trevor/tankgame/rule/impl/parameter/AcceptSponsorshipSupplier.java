package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

import java.util.ArrayList;
import java.util.List;

public class AcceptSponsorshipSupplier implements AvailableParameterSupplier<PlayerRef> {
    @Override
    public ParameterBound<PlayerRef> possibleParameters(State state, Player player) {
        Tank tank = state.getTankForPlayerRef(player.toRef()).get();
        ListEntity<PlayerRef> offeredSponsors = tank.getOrElse(Attribute.OFFERED_SPONSORS, new ListEntity<>());
        List<PlayerRef> offeredSponsorsList = new ArrayList<>(offeredSponsors);
        return new DiscreteValueBound<>(Attribute.TARGET_PLAYER, offeredSponsorsList);
    }
}
