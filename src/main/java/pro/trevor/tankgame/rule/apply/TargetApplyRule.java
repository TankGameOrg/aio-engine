package pro.trevor.tankgame.rule.apply;

import java.util.Optional;

public class TargetApplyRule<T> extends ApplyRule {
    public TargetApplyRule(TargetApply<T> targetTick, Class<T> target) {
        super((state) -> {
            state.gather(target).forEach((t) -> targetTick.apply(state, t));
            return Optional.empty();
        });
    }
}
