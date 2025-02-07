package pro.trevor.tankgame.state.board.floor;

import org.json.JSONObject;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.util.JsonType;

@JsonType(name = "ScrapHeap")
public class ScrapHeap extends WalkableFloor {

    public ScrapHeap(Position position) {
        super(position);
    }

    public ScrapHeap(JSONObject json) {
        super(json);
    }

    @Override
    public char toBoardCharacter() {
        return 'G';
    }
}
