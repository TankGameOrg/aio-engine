package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.Precondition;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;

import java.util.Optional;

public class PlayerTankHasAttributePrecondition implements Precondition {

    private final Attribute<?> attribute;

    public PlayerTankHasAttributePrecondition(Attribute<?> attribute) {
        this.attribute = attribute;
    }

    @Override
    public Error test(State state, Player player) {
        Optional<Tank> optionalTank = state.getTankForPlayerRef(player.toRef());
        if (optionalTank.isEmpty()) {
            return new Error(Error.Type.PRECONDITION, "Player has no tank");
        }

        Tank tank = optionalTank.get();
        if (tank.has(attribute)) {
            return Error.NONE;
        } else {
            return new Error(Error.Type.OTHER, "Player tank does not have the attribute: " + attribute.getName());
        }
    }
}
