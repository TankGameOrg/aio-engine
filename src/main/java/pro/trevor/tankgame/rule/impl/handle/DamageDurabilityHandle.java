package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.handle.DamageHandle;
import pro.trevor.tankgame.rule.handle.cause.Cause;
import pro.trevor.tankgame.rule.handle.cause.TankCause;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.util.IRandom;
import pro.trevor.tankgame.util.Random;

public class DamageDurabilityHandle implements DamageHandle {

    private final int[] distribution;

    public DamageDurabilityHandle() {
        this.distribution = new int[]{1};
    }

    public DamageDurabilityHandle(int[] distribution) {
        this.distribution = distribution;
    }

    @Override
    public void damage(State state, Cause cause, AttributeEntity target, LogEntry entry) {

        IRandom random = state.getOrElse(Attribute.RANDOM, new Random(System.currentTimeMillis()));
        int roll = random.nextInt(distribution.length);
        state.put(Attribute.RANDOM, random);

        int damage = distribution[roll];
        int durability = target.getOrElse(Attribute.DURABILITY, 0);

        int attackModifier = 0;
        if (cause.getCause() instanceof AttributeEntity entity) {
            attackModifier = entity.getOrElse(Attribute.DAMAGE_MODIFIER, 0);
        }

        int defenseModifier = target.getOrElse(Attribute.DEFENSE_MODIFIER, 0);
        int totalDamage = damage + attackModifier - defenseModifier;

        // If we might apply negative damage, set the damage to zero
        if (totalDamage < 0) {
            totalDamage = 0;
        }

        entry.put(Attribute.DICE_ROLL, damage);
        entry.put(Attribute.TOTAL_DAMAGE, totalDamage);

        int newDurability = Math.max(0, durability - totalDamage);
        target.put(Attribute.DURABILITY, newDurability);
    }
}
