package pro.trevor.tankgame.rule.action.parameter;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

public class Parameter<T> {

    private final String name;
    private final Attribute<T> attribute;
    private final AvailableParameterSupplier<T> supplier;

    public Parameter(String name, Attribute<T> attribute, AvailableParameterSupplier<T> supplier) {
        this.name = name;
        this.attribute = attribute;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public Attribute<T> getAttribute() {
        return attribute;
    }

    public ParameterBound<T> possibleParameters(State state, Player player) {
        return supplier.possibleParameters(state, player);
    }

    public boolean entryHasParameter(LogEntry entry) {
        return entry.has(attribute);
    }
}
