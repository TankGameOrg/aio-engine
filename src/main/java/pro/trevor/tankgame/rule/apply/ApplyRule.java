package pro.trevor.tankgame.rule.apply;

import pro.trevor.tankgame.state.State;

public class ApplyRule {

    private final Apply apply;

    public ApplyRule(Apply apply) {
        this.apply = apply;
    }

    public void apply(State state) {
        apply.apply(state);
    }

}
