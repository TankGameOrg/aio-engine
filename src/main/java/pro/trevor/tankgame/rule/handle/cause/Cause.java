package pro.trevor.tankgame.rule.handle.cause;

public sealed interface Cause permits EnvironmentCause, NoCause, PlayerCause {
    Object getCause();
}
