package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.rule.impl.action.upgrade.Boon;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoonSupplier implements AvailableParameterSupplier<Boon> {

    private final List<Boon> values;

    public BoonSupplier(List<Boon> values) {
        this.values = new ArrayList<>(values);
    }


    @Override
    public ParameterBound<Boon> possibleParameters(State state, Player player) {
        return new DiscreteValueBound<>(Attribute.TARGET_BOON, values);
    }
}
