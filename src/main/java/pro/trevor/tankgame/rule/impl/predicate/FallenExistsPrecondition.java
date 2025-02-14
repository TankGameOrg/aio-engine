package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.Precondition;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.PseudoTank;
import pro.trevor.tankgame.state.meta.Player;

public class FallenExistsPrecondition implements Precondition {
    @Override
    public Error test(State state, Player player) {
        if (state.gather(PseudoTank.class).findAny().isEmpty()) {
            return new Error(Error.Type.PRECONDITION, "Fallen is not present");
        } else {
            return Error.NONE;
        }
    }
}
