package pro.trevor.tankgame.rule.handle;

import pro.trevor.tankgame.attribute.AttributeEntity;

import java.util.function.Predicate;

public class Destroy {

    private final Predicate<AttributeEntity> predicate;
    private final DestroyHandle handle;

    public Destroy(Predicate<AttributeEntity> predicate, DestroyHandle handle) {
        this.predicate = predicate;
        this.handle = handle;
    }

    public Predicate<AttributeEntity> getPredicate() {
        return predicate;
    }

    public DestroyHandle getHandle() {
        return handle;
    }
}
