package pro.trevor.tankgame.rule.handle.cause;

public sealed interface Cause permits EnvironmentCause, NoCause, PseudoTankCause, TankCause {
    Object getCause();
}
