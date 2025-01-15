package pro.trevor.tankgame.rule.action.parameter;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.Codec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DiscreteValueBound<T> implements DiscreteParameterBound<T> {

    private final Attribute<T> attribute;
    private final List<T> values;

    @SafeVarargs
    public DiscreteValueBound(Attribute<T> attribute, T... values) {
        this.attribute = attribute;
        this.values = Arrays.asList(values);
    }

    public DiscreteValueBound(Attribute<T> attribute, List<T> values) {
        this.attribute = attribute;
        this.values = new ArrayList<>(values);
    }

    @Override
    public Attribute<T> getAttribute() {
        return attribute;
    }

    @Override
    public List<T> values() {
        return values;
    }
}
