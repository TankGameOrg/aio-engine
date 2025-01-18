package pro.trevor.tankgame.web.server;

import org.json.JSONObject;
import pro.trevor.tankgame.Game;
import pro.trevor.tankgame.rule.RulesetRegistry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.util.IJsonObject;

import java.util.UUID;

public record GameInfo(String name, UUID uuid, Game game) implements IJsonObject {

    public static final String NAME = "name";
    public static final String UUID = "uuid";
    public static final String STATE = "state";
    public static final String RULESET = "ruleset";

    public GameInfo(JSONObject json) {
        this(json.getString(NAME),
                java.util.UUID.fromString(json.getString(UUID)),
                RulesetRegistry.REGISTRY.createGame(json.getString(RULESET)).orElseThrow()
        );
        this.game.setState(new State(json.getJSONObject(STATE)));
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put(NAME, name);
        result.put(UUID, uuid.toString());
        result.put(RULESET, game.getRulesetIdentifier());
        result.put(STATE, game.getState().toJson());
        return result;
    }

    public JSONObject toJsonWithoutState() {
        JSONObject result = new JSONObject();
        result.put(NAME, name);
        result.put(UUID, uuid.toString());
        result.put(RULESET, game.getRulesetIdentifier());
        return result;
    }
}
