package pro.trevor.tankgame.rule.impl.apply;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.rule.apply.TargetApply;
import pro.trevor.tankgame.state.State;

public class ModifyAttribute<T extends AttributeEntity, A> implements TargetApply<T> {

    public interface ModifyAttributeFunction<T, A> {
        A apply(State state, T target);
    }

    private final Attribute<A> attribute;
    private final ModifyAttributeFunction<T, A> function;

    public ModifyAttribute(Attribute<A> attribute, ModifyAttributeFunction<T, A> attributeFunction) {
        this.attribute = attribute;
        this.function = attributeFunction;
    }

    @Override
    public void apply(State state, T target) {
        target.put(attribute, function.apply(state, target));
    }
}
