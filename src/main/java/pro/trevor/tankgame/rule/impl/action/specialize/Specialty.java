package pro.trevor.tankgame.rule.impl.action.specialize;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.Arrays;
import java.util.List;

@JsonType(name = "Specialty")
public enum Specialty implements IJsonObject {
    OFFENSE(new AttributeModifier(Attribute.DAMAGE_MODIFIER, 1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1)),
    DEFENSE(new AttributeModifier(Attribute.DAMAGE_MODIFIER, -1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, 2)),
    SCOUT(new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1), new AttributeModifier(Attribute.SPEED, 1), new AttributeModifier(Attribute.RANGE, 1)),
    MELEE(new AttributeModifier(Attribute.DAMAGE_MODIFIER, 1), new AttributeModifier(Attribute.DEFENSE_MODIFIER, -1), new AttributeModifier(Attribute.SPEED, 1), new AttributeModifier(Attribute.RANGE, -1))
    ;

    final List<AttributeModifier> modifiers;

    Specialty(AttributeModifier... modifiers) {
        this.modifiers = Arrays.asList(modifiers);
    }

    void apply(AttributeEntity entity) {
        for (AttributeModifier modifier : modifiers) {
            entity.put(modifier.attribute(), entity.getOrElse(modifier.attribute(), 0) + modifier.modifier());
        }
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}