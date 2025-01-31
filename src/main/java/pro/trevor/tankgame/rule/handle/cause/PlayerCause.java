package pro.trevor.tankgame.rule.handle.cause;

import pro.trevor.tankgame.state.meta.Player;

public final class PlayerCause implements Cause {

    private final Player cause;

    public PlayerCause(Player cause) {
        this.cause = cause;
    }

    @Override
    public Player getCause() {
        return cause;
    }
}
