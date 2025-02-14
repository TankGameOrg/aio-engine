package pro.trevor.tankgame.rule.apply;

import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;

import java.util.Optional;

public interface Apply {
    Optional<LogEntry> apply(State state);
}
