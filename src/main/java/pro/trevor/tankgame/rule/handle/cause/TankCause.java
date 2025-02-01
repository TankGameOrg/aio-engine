package pro.trevor.tankgame.rule.handle.cause;

import pro.trevor.tankgame.state.board.unit.Tank;

public final class TankCause implements Cause {

    private final Tank cause;

    public TankCause(Tank cause) {
        this.cause = cause;
    }

    @Override
    public Tank getCause() {
        return cause;
    }
}
