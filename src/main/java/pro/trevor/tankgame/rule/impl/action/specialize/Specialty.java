package pro.trevor.tankgame.rule.impl.action.specialize;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonType(name = "Specialty")
public class Specialty implements IJsonObject {

    private final String name;
    private final List<AttributeModifier> modifiers;


    public Specialty(String name, AttributeModifier... modifiers) {
        this.name = name;
        this.modifiers = Arrays.asList(modifiers);
    }

    public Specialty(JSONObject jsonObject) {
        this.name = jsonObject.getString("name");

        ArrayList<AttributeModifier> modifiers = new ArrayList<>();
        JSONArray modifiersArray = jsonObject.getJSONArray("modifiers");
        for (int i = 0; i < modifiersArray.length(); ++i) {
            AttributeModifier modifier = new AttributeModifier(modifiersArray.getJSONObject(i));
            modifiers.add(modifier);
        }
        this.modifiers = modifiers;
    }

    void apply(AttributeEntity entity) {
        for (AttributeModifier modifier : modifiers) {
            modifier.apply(entity);
        }
    }

    void remove(AttributeEntity entity) {
        for (AttributeModifier modifier : modifiers) {
            modifier.remove(entity);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        JSONArray modifiersArray = new JSONArray();
        for (AttributeModifier modifier : modifiers) {
            modifiersArray.put(modifier.toJson());
        }
        result.put("modifiers", modifiersArray);
        result.put("name", name);

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Specialty specialty)) return false;
        return Objects.equals(name, specialty.name) && Objects.equals(modifiers, specialty.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, modifiers);
    }
}