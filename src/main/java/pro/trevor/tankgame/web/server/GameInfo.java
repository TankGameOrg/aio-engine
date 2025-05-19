package pro.trevor.tankgame.web.server;

import org.json.JSONObject;
import pro.trevor.tankgame.rule.Ruleset;
import pro.trevor.tankgame.rule.RulesetRegister;
import pro.trevor.tankgame.rule.RulesetRegistry;
import pro.trevor.tankgame.rule.gameday.OpenHours;
import pro.trevor.tankgame.util.IJsonObject;

import java.util.Objects;
import java.util.UUID;

public final class GameInfo implements IJsonObject {

    public static final String NAME = "name";
    public static final String UUID = "uuid";
    public static final String RULESET = "ruleset";
    public static final String OPEN_HOURS = "open_hours";

    private final String name;
    private final UUID uuid;
    private final OpenHours openHours;
    private final RulesetRegister rulesetRegister;

    private final Ruleset ruleset;

    public GameInfo(String name, UUID uuid, OpenHours openHours, RulesetRegister rulesetRegister) {
        this.name = name;
        this.uuid = uuid;
        this.openHours = openHours;
        this.rulesetRegister = rulesetRegister;
        this.ruleset = new Ruleset(rulesetRegister);
    }

    public GameInfo(JSONObject json) {
        this.name = json.getString(NAME);
        this.uuid = java.util.UUID.fromString(json.getString(UUID));
        this.openHours = new OpenHours(json.optJSONObject(OPEN_HOURS, new OpenHours().toJson()));
        this.rulesetRegister = RulesetRegistry.REGISTRY.fromString(json.getString(RULESET)).orElseThrow();
        this.ruleset = new Ruleset(rulesetRegister);
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put(NAME, name);
        result.put(UUID, uuid.toString());
        result.put(RULESET, rulesetRegister.getIdentifier());
        result.put(OPEN_HOURS, openHours.toJson());
        return result;
    }

    public String name() {
        return name;
    }

    public UUID uuid() {
        return uuid;
    }

    public OpenHours openHours() {
        return openHours;
    }

    public RulesetRegister rulesetRegister() {
        return rulesetRegister;
    }

    public Ruleset ruleset() {
        return ruleset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GameInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.uuid, that.uuid) &&
                Objects.equals(this.openHours, that.openHours) &&
                Objects.equals(this.rulesetRegister.getIdentifier(), that.rulesetRegister.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uuid, openHours, rulesetRegister.getIdentifier());
    }

    @Override
    public String toString() {
        return "GameInfo[" +
                "name=" + name + ", " +
                "uuid=" + uuid + ", " +
                "openHours=" + openHours + ", " +
                "rulesetIdentifier=" + rulesetRegister.getIdentifier() + ']';
    }

}
