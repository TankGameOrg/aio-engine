package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;

import java.util.function.Predicate;

public class HasZeroDurabilityPredicate implements Predicate<AttributeEntity> {
    @Override
    public boolean test(AttributeEntity entity) {
        return entity.has(Attribute.DURABILITY) && entity.getUnsafe(Attribute.DURABILITY) == 0;
    }
}
