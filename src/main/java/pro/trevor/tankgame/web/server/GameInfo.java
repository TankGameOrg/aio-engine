package pro.trevor.tankgame.web.server;

import org.json.JSONObject;
import pro.trevor.tankgame.Game;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.util.IJsonObject;

import java.util.UUID;

public record GameInfo(String name, UUID uuid, Game game) implements IJsonObject {
    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("name", name);
        result.put("uuid", uuid.toString());
        result.put("ruleset", game.getRulesetIdentifier());
        result.put("running", game.getState().getOrElse(Attribute.RUNNING, true));
        result.put("state", game.getState().toJson());
        return result;
    }
}
