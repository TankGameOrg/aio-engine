package pro.trevor.tankgame.rule.action;

import java.util.Map;

import org.json.JSONObject;

import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

@JsonType(name = "LogEntry")
public class LogEntry extends AttributeEntity implements IJsonObject {
    public LogEntry() {
        super();
    }

    public LogEntry(Map<Attribute<?>, ?> defaults) {
        super(defaults);
    }

    public LogEntry(JSONObject json) {
        super(json);
    }

    @Override
    protected String toAttributeJsonKeyString(String attribute) {
        return attribute.toLowerCase();
    }

    @Override
    protected String toAttributeString(String attributeKey) {
        return attributeKey.toUpperCase();
    }

    @Override
    protected boolean isAttributeJsonKey(String attribute) {
        return true;
    }
}
