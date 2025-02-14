package pro.trevor.tankgame.rule.impl.action.fallen;

import org.json.JSONObject;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

@JsonType(name = "ActionType")
public enum ActionType implements IJsonObject {
    REMAIN,
    MOVE,
    SHOOT
    ;

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}
