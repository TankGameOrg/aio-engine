package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.DestroyHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.rule.handle.cause.TankCause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.board.unit.Wall;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.Position;

public class DestroyTankHandle implements DestroyHandle {

    private final int wallDurability;

    public DestroyTankHandle(int wallDurability) {
        this.wallDurability = wallDurability;
    }

    @Override
    public void destroy(State state, Cause cause, AttributeEntity target, LogEntry entry) {
        if (!(target instanceof Tank tank)) {
            return;
        }

        if (cause instanceof TankCause tankCause && !tankCause.getCause().equals(target)) {
            Tank causeTank = tankCause.getCause();
            Player player = causeTank.getPlayerRef().toPlayer(state).get();

            int previousScrap = player.getOrElse(Attribute.SCRAP, 0);
            int tankScrap = tank.getOrElse(Attribute.SCRAP, 0);
            int tankHasSponsorScrap = tank.has(Attribute.SPONSOR) ? 2 : 0;

            player.put(Attribute.SCRAP,  previousScrap + tankScrap + tankHasSponsorScrap);
        }

        Position targetPosition = tank.getPosition();
        state.getBoard().putUnit(new Wall(targetPosition, wallDurability));
    }
}
