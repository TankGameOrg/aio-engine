package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.DestroyHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.rule.handle.cause.TankCause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.unit.Tank;

import java.util.HashMap;
import java.util.Map;

public class RewardScrapHandle implements DestroyHandle {

    private final Map<Class<?>, Integer> rewardScrap;

    public RewardScrapHandle(Map<Class<?>, Integer> rewardScrap) {
        this.rewardScrap = new HashMap<>(rewardScrap);
    }

    private int rewardForExactClass(Class<?> type) {
        return rewardScrap.getOrDefault(type, 0);
    }

    @Override
    public void destroy(State state, Cause cause, AttributeEntity target, LogEntry entry) {
        if (cause instanceof TankCause tankCause && !tankCause.getCause().equals(target)) {
            Tank causeTank = tankCause.getCause();

            int previousScrap = causeTank.getOrElse(Attribute.SCRAP, 0);
            int reward = rewardForExactClass(target.getClass());

            causeTank.put(Attribute.SCRAP,  previousScrap + reward);
        }
    }
}
