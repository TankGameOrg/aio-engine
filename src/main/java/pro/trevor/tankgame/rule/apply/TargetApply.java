package pro.trevor.tankgame.rule.apply;

import pro.trevor.tankgame.state.State;

public interface TargetApply<T> {
    void apply(State state, T target);
}
