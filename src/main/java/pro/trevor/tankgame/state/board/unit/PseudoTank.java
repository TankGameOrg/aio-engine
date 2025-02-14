package pro.trevor.tankgame.state.board.unit;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.state.board.Element;
import pro.trevor.tankgame.state.board.IUnit;
import pro.trevor.tankgame.util.JsonType;
import pro.trevor.tankgame.util.Position;

@JsonType(name = "PseudoTank")
public class PseudoTank extends Element implements IUnit {

    public PseudoTank(Position position) {
        super();
        put(Attribute.POSITION, position);
    }

    public PseudoTank(JSONObject json) {
        super(json);
    }

    @Override
    public char toBoardCharacter() {
        return 'P';
    }
}
