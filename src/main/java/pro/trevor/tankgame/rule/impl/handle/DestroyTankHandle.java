package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.handle.DestroyHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.board.unit.Wall;
import pro.trevor.tankgame.util.Position;

public class DestroyTankHandle implements DestroyHandle {

    private final int wallDurability;

    public DestroyTankHandle(int wallDurability) {
        this.wallDurability = wallDurability;
    }

    @Override
    public void destroy(State state, Cause cause, AttributeEntity target) {
        if (!(target instanceof Tank tank)) {
            return;
        }

        Position targetPosition = tank.getPosition();
        state.getBoard().putUnit(new Wall(targetPosition, wallDurability));
    }
}
