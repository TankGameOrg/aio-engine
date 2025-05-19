package pro.trevor.tankgame.state.builder;

import pro.trevor.tankgame.Game;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.rule.gameday.OpenHours;
import pro.trevor.tankgame.rule.impl.ruleset.DefaultV2RulesetRegistry;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.Element;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.board.unit.Wall;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;
import pro.trevor.tankgame.web.server.Environment;
import pro.trevor.tankgame.web.server.GameInfo;
import pro.trevor.tankgame.web.server.Storage;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SurroundedByWalls {

    public static void makeGame(int width, int height, List<String> names) {
        assert width >= 8;
        assert height >= 8;
        assert names.size() == 16;

        Builder builder = new Builder(width, height, names);

        builder.template("wall1", new Wall(new Position(0, 0), 1));
        builder.template("wall2", new Wall(new Position(0, 0), 2));

        int halfWidth = (width - 1) / 2;
        int halfHeight = (height - 1) / 2;

        for (int i = 0; i < width; ++i) {
            builder.placeElement(builder.instance("wall1"), new Position(i, halfHeight));
            if (height % 2 == 0) {
                builder.placeElement(builder.instance("wall1"), new Position(i, halfHeight + 1));
            }
        }
        for (int i = 0; i < height; ++i) {
            builder.placeElement(builder.instance("wall1"), new Position(halfWidth, i));
            if (height % 2 == 0) {
                builder.placeElement(builder.instance("wall1"), new Position(halfWidth + 1, i));
            }
        }

        for (int i = 0; i < width; ++i) {
            builder.placeElement(builder.instance("wall2"), new Position(i, 0));
            builder.placeElement(builder.instance("wall2"), new Position(i, height - 1));
        }
        for (int i = 0; i < height; ++i) {
            builder.placeElement(builder.instance("wall2"), new Position(0, i));
            builder.placeElement(builder.instance("wall2"), new Position(width - 1, i));
        }

        Tank tankTemplate = new Tank(new PlayerRef("None"), new Position(0, 0), Map.of());
        tankTemplate.put(Attribute.SCRAP, 4);
        tankTemplate.put(Attribute.DURABILITY, 12);
        tankTemplate.put(Attribute.DAMAGE_MODIFIER, 0);
        tankTemplate.put(Attribute.DEFENSE_MODIFIER, 0);
        tankTemplate.put(Attribute.SPEED, 2);
        tankTemplate.put(Attribute.RANGE, 2);
        tankTemplate.put(Attribute.CAN_ACT, true);
        builder.template("tank", tankTemplate);

        String[] teams = new String[]{"Red", "Yellow", "Blue", "Green"};
        Position[] basePositions = new Position[]{new Position(0, 0), new Position(0, 6), new Position(6, 0), new Position(6, 6)};
        Position[] offsets = new Position[]{new Position(2, 2), new Position(2, 3), new Position(3, 2), new Position(3, 3)};
        for (int i = 0; i < basePositions.length; ++i) {
            Position basePosition = basePositions[i];
            String team = teams[i];

            for (Position offsetPosition : offsets) {
                Element tank = builder.instance("tank");
                tank.put(Attribute.TEAM, team);
                builder.placeElement(tank, new Position(basePosition.x() + offsetPosition.x(), basePosition.y() + offsetPosition.y()));
            }
        }

        builder.modifyPlayers((player) -> {
        });
        State state = builder.build();

        Game game = new Game(new DefaultV2RulesetRegistry(), state);
        GameInfo gameInfo = new GameInfo("Tank Game Redux^2", UUID.randomUUID(), new OpenHours(), game.getRulesetRegister());
        Storage storage = new Storage(new File(Environment.getStoragePath()));
        storage.saveGameInitialState(gameInfo, game);
    }

}
