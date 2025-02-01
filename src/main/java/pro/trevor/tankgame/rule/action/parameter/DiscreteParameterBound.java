package pro.trevor.tankgame.rule.action.parameter;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Codec;
import pro.trevor.tankgame.util.IJsonObject;

import java.util.List;

public interface DiscreteParameterBound<T> extends ParameterBound<T> {

    List<T> values();

    @Override
    default Type getBoundType() {
        return Type.DISCRETE;
    }

    @Override
    default JSONObject toJson() {
        JSONObject result = ParameterBound.super.toJson();

        JSONArray valuesArray = new JSONArray();
        for (T value : values()) {
            if (value instanceof IJsonObject jsonObject) {
                valuesArray.put(Codec.encodeJson(jsonObject));
            } else {
                valuesArray.put(value);
            }
        }

        result.put("values", valuesArray);

        return result;
    }
}
