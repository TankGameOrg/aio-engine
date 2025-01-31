package pro.trevor.tankgame.rule.handle;

import pro.trevor.tankgame.attribute.AttributeEntity;

import java.util.function.Predicate;

public class Damage {

    private final Predicate<AttributeEntity> predicate;
    private final DamageHandle handle;

    public Damage(Predicate<AttributeEntity> predicate, DamageHandle handle) {
        this.predicate = predicate;
        this.handle = handle;
    }

    public Predicate<AttributeEntity> getPredicate() {
        return predicate;
    }

    public DamageHandle getHandle() {
        return handle;
    }
}
