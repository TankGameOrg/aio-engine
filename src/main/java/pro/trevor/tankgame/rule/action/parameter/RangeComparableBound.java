package pro.trevor.tankgame.rule.action.parameter;

import pro.trevor.tankgame.attribute.Attribute;

public class RangeComparableBound<T extends Comparable<T>> implements RangeParameterBound<T> {

    private final Attribute<T> attribute;
    private final T min;
    private final T max;

    public RangeComparableBound(Attribute<T> attribute, T min, T max) {
        this.attribute = attribute;
        this.min = min;
        this.max = max;
    }

    @Override
    public Attribute<T> getAttribute() {
        return attribute;
    }

    @Override
    public T min() {
        return min;
    }

    @Override
    public T max() {
        return max;
    }
}
