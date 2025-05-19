package pro.trevor.tankgame.rule.impl.action.specialize;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.Objects;
import java.util.Optional;

@JsonType(name = "Modifier")
public class AttributeModifier implements IJsonObject {
    private final Attribute<Integer> attribute;
    private final int modifier;

    public AttributeModifier(Attribute<Integer> attribute, int modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public AttributeModifier(JSONObject json) {
        Optional<Attribute<Integer>> maybeAttribute = Attribute.fromString(json.getString("attribute"), Integer.class);
        this.attribute = maybeAttribute.get();
        this.modifier = json.getInt("modifier");
    }

    public void apply(AttributeEntity entity) {
        entity.put(attribute, entity.getOrElse(attribute, 0) + modifier);
    }

    public void remove(AttributeEntity entity) {
        entity.put(attribute, entity.getOrElse(attribute, 0) - modifier);
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("attribute", attribute.getName());
        result.put("modifier", modifier);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AttributeModifier that)) return false;
        return modifier == that.modifier && Objects.equals(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, modifier);
    }
}
