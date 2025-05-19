package pro.trevor.tankgame.rule.impl.action.upgrade;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.impl.action.specialize.AttributeModifier;
import pro.trevor.tankgame.util.JsonType;

import java.util.Objects;

@JsonType(name = "Boon")
public class Boon extends AttributeModifier {

    private final String name;

    public Boon(String name, Attribute<Integer> attribute, int modifier) {
        super(attribute, modifier);
        this.name = name;
    }

    public Boon(JSONObject json) {
        super(json);
        name = json.getString("name");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("name", name);
        return json;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Boon boon)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(name, boon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
