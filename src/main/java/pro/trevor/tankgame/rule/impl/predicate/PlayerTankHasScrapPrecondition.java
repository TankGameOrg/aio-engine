package pro.trevor.tankgame.rule.impl.predicate;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.action.Error;
import pro.trevor.tankgame.rule.action.Precondition;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Player;

import java.util.Optional;

public class PlayerTankHasScrapPrecondition implements Precondition {

    private final int scrap;

    public PlayerTankHasScrapPrecondition(int scrap) {
        this.scrap = scrap;
    }


    @Override
    public Error test(State state, Player player) {
        Optional<Tank> maybeTank = state.getTankForPlayerRef(player.toRef());
        if (maybeTank.isEmpty()) {
            return new Error(Error.Type.PRECONDITION, "Player has no tank");
        }

        Tank tank = maybeTank.get();

        if (tank.getOrElse(Attribute.SCRAP, 0) < scrap) {
            return new Error(Error.Type.OTHER, "Tank has insufficient scrap");
        }

        return Error.NONE;
    }
}
