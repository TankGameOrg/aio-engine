package pro.trevor.tankgame.state.builder;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.*;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Council;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.util.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

public class Builder {

    private final Map<String, Element> templates;
    private final Board board;
    private final ListEntity<Player> players;

    public Builder(int width, int height, List<String> players) {
        this.templates = new HashMap<>();
        this.board = new Board(width, height);

        players = new ArrayList<>(players);
        this.players = new ListEntity<>();
        Collections.shuffle(players);
        for (String player : players) {
            this.players.add(new Player(player));
        }
    }

    public void template(String name, Element template) {
        this.templates.put(name, template);
    }

    public Element instance(String templateName) {
        try {
            Element element = templates.get(templateName);
            return element.getClass().getConstructor(JSONObject.class).newInstance(element.toJson());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyPlayers(Consumer<Player> action) {
        for (Player player : players) {
            action.accept(player);
        }
    }

    public void placeElement(IElement element, Position position) {
        element.setPosition(position);
        if (element instanceof IUnit unit) {
            board.putUnit(unit);
        } else if (element instanceof IFloor floor) {
            board.putFloor(floor);
        } else {
            throw new Error("Unknown element placed");
        }

    }

    public State build() {
        List<Tank> tanks = board.gather(Tank.class).toList();

        assert tanks.size() == players.size();

        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            Player player = players.get(i);
            player.put(Attribute.TEAM, tank.getUnsafe(Attribute.TEAM));
            tank.put(Attribute.PLAYER_REF, player.toRef());
        }
        return new State(board, new Council(), players);
    }

}
