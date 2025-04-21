package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.Precondition;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.meta.Player;

public class CouncilorCanBlessPrecondition implements Precondition {
    @Override
    public Error test(State state, Player player) {
        if (!player.getOrElse(Attribute.CAN_BLESS, true)) {
            return new Error(Error.Type.OTHER, "Player has already blessed their patron");
        }

        return Error.NONE;
    }
}
