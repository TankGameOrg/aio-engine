package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.handle.DamageHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.state.State;

public class DamageDurabilityHandle implements DamageHandle {
    @Override
    public void damage(State state, Cause cause, AttributeEntity target, int damage) {
        int durability = target.getOrElse(Attribute.DURABILITY, 0);
        int modifier = target.getOrElse(Attribute.DEFENSE_MODIFIER, 0);
        int totalDamage = damage - modifier;

        // If we might do nothing or apply negative damage, skip the application
        if (totalDamage <= 0) {
            return;
        }

        int newDurability = Math.max(0, durability - totalDamage);
        target.put(Attribute.DURABILITY, newDurability);
    }
}
