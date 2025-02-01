package pro.trevor.tankgame.rule.impl.action.upgrade;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.attribute.Codec;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

@JsonType(name = "Boon")
public enum Boon implements IJsonObject {
    Attack(Attribute.DAMAGE_MODIFIER, 1),
    Defense(Attribute.DEFENSE_MODIFIER, 1),
    Speed(Attribute.SPEED, 1),
    Range(Attribute.RANGE, 1)
    ;

    private final Attribute<Integer> attribute;
    private final int modifier;

    Boon(Attribute<Integer> attribute, int modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public void apply(AttributeEntity entity) {
        entity.put(attribute, entity.getOrElse(attribute, 0) + modifier);
    }

    public void remove(AttributeEntity entity) {
        entity.put(attribute, entity.getOrElse(attribute, 0) - modifier);
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}
