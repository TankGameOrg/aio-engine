package pro.trevor.tankgame.rule.action.parameter;

import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

public interface AvailableParameterSupplier<T> {
    ParameterBound<T> possibleParameters(State state, Player player);
}
