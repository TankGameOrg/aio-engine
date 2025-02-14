package pro.trevor.tankgame.rule.apply;

import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;

import java.util.Optional;

public class ApplyRule {

    private final Apply apply;

    public ApplyRule(Apply apply) {
        this.apply = apply;
    }

    public Optional<LogEntry> apply(State state) {
        return apply.apply(state);
    }

}
