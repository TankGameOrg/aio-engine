package pro.trevor.tankgame.rule.impl.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.parameter.AvailableParameterSupplier;
import pro.trevor.tankgame.rule.action.parameter.DiscreteValueBound;
import pro.trevor.tankgame.rule.action.parameter.ParameterBound;
import pro.trevor.tankgame.rule.impl.action.specialize.Specialty;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecialtySupplier implements AvailableParameterSupplier<Specialty> {

    private final List<Specialty> specialties;

    public SpecialtySupplier(List<Specialty> specialties) {
        this.specialties = new ArrayList<>(specialties);
    }

    @Override
    public ParameterBound<Specialty> possibleParameters(State state, Player player) {
        return new DiscreteValueBound<>(Attribute.TARGET_SPECIALTY, specialties);
    }
}
