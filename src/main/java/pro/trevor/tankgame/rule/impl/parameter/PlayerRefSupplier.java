package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;

public class PlayerRefSupplier implements AvailableParameterSupplier<PlayerRef> {
    @Override
    public ParameterBound<PlayerRef> possibleParameters(State state, Player player) {
        return new DiscreteValueBound<>(Attribute.PLAYER_REF, state.getPlayers().stream().map(Player::toRef).toList());
    }
}
