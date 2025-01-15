package pro.trevor.tankgame.rule.action.parameter;

import org.json.JSONObject;

public interface RangeParameterBound<T> extends ParameterBound<T> {

    T min();
    T max();

    @Override
    default Type getBoundType() {
        return Type.RANGE;
    }

    @Override
    default JSONObject toJson() {
        JSONObject result = ParameterBound.super.toJson();
        result.put("min", min());
        result.put("max", max());
        return result;
    }
}
