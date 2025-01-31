package pro.trevor.tankgame.rule.impl.handle;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;

import java.util.function.Predicate;

public class HasDurabilityPredicate implements Predicate<AttributeEntity> {
    @Override
    public boolean test(AttributeEntity entity) {
        return entity.has(Attribute.DURABILITY);
    }
}
