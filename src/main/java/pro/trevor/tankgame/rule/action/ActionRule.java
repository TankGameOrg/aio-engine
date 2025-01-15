package pro.trevor.tankgame.rule.action;

import pro.trevor.tankgame.rule.action.parameter.Parameter;
import pro.trevor.tankgame.state.State;

import java.util.Arrays;
import java.util.List;

public class ActionRule {

    private final Predicate predicate;
    private final Action action;
    private final List<Parameter<?>> parameters;

    public ActionRule(Predicate predicate, Action action, Parameter<?>... parameters) {
        this.predicate = predicate;
        this.action = action;
        this.parameters = Arrays.asList(parameters);
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public Error apply(State state, LogEntry entry) {
        Error result = predicate.test(state, entry);
        if (result == Error.NONE) {
            return action.apply(state, entry);
        } else {
            return result;
        }
    }

}
