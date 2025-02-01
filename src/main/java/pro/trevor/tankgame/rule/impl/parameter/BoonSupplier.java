package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.rule.impl.action.upgrade.Boon;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

import java.util.Arrays;

public class BoonSupplier implements AvailableParameterSupplier<Boon> {
    @Override
    public ParameterBound<Boon> possibleParameters(State state, Player player) {
        return new DiscreteValueBound<>(Attribute.TARGET_BOON, Arrays.asList(Boon.values()));
    }
}
