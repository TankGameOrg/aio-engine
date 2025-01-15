package pro.trevor.tankgame.rule.action.parameter;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.Codec;
import pro.trevor.tankgame.util.IJsonObject;

public interface ParameterBound<T> extends IJsonObject {
    enum Type {
        DISCRETE,
        RANGE
    }

    Attribute<T> getAttribute();
    Type getBoundType();

    @Override
    default JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("attribute", getAttribute().getName());
        result.put("type", Codec.typeFromClass(getAttribute().getAttributeClass()));
        result.put("bound", getBoundType().toString());
        return result;
    }
}
