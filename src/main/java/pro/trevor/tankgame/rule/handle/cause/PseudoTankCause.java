package pro.trevor.tankgame.rule.handle.cause;

import pro.trevor.tankgame.state.board.unit.PseudoTank;

public final class PseudoTankCause implements Cause {

    private final PseudoTank pseudoTank;

    public PseudoTankCause(PseudoTank pseudoTank) {
        this.pseudoTank = pseudoTank;
    }


    @Override
    public PseudoTank getCause() {
        return pseudoTank;
    }
}
