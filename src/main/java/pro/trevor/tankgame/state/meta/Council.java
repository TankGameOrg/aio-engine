package pro.trevor.tankgame.state.meta;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.attribute.AttributeEntity;
import pro.trevor.tankgame.util.IJsonObject;
import pro.trevor.tankgame.util.JsonType;

@JsonType(name = "Council")
public class Council extends AttributeEntity implements IJsonObject {

    public Council() {
        super();
        put(Attribute.COUNCILLORS, new ListEntity<>());
    }

    public Council(JSONObject json) {
        super(json);
    }

    public ListEntity<PlayerRef> getCouncillors() {
        return getUnsafe(Attribute.COUNCILLORS);
    }

    public boolean isPlayerOnCouncil(PlayerRef playerRef) {
        return getCouncillors().stream().anyMatch(playerRef::equals);
    }
}
