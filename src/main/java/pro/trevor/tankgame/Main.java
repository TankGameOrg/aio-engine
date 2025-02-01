package pro.trevor.tankgame;

import org.json.JSONObject;
import pro.trevor.tankgame.attribute.Attribute;
import pro.trevor.tankgame.attribute.ListEntity;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.rule.impl.ruleset.DefaultRulesetRegister;
import pro.trevor.tankgame.state.State;
import pro.trevor.tankgame.state.board.Board;
import pro.trevor.tankgame.state.board.unit.Tank;
import pro.trevor.tankgame.state.meta.Council;
import pro.trevor.tankgame.state.meta.Player;
import pro.trevor.tankgame.state.meta.PlayerRef;
import pro.trevor.tankgame.util.Position;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-v") || args[0].equals("--version")) {
                Main.printVersion();
            } else if (args[0].equals("-t") || args[0].equals("--test")) {
                runTest();
            } else {
                System.err.println("Unknown parameter: " + args[0]);
                System.exit(1);
            }
        } else if (args.length == 0) {
            // TODO
            System.err.println("CLI is not yet implemented");
            System.exit(1);
        } else {
            System.err.println("Expected 0 or 1 arguments:\n    tankgame <-v|--version>");
            System.exit(1);
        }
    }

    /**
     * Print the program version as JSON.
     */
    private static void printVersion() {
        JSONObject versionInfo = new JSONObject();
        String version = Main.class.getPackage().getImplementationVersion();
        versionInfo.put("version", version);
        System.out.println(versionInfo.toString(4));
    }

    /**
     * Run the current debugging test.
     */
    private static void runTest() {
        Game game = new Game(new DefaultRulesetRegister(), new State(new Board(1, 1), new Council(), new ListEntity<>(List.of(new Player("TestPlayer")))));
        game.getState().getBoard().putUnit(new Tank(new PlayerRef("TestPlayer"), new Position(0, 0), Map.of()));

        LogEntry actionEntry = new LogEntry();
        actionEntry.put(Attribute.ACTION, "ExampleAction");
        actionEntry.put(Attribute.SUBJECT, new PlayerRef("TestPlayer"));
        actionEntry.put(Attribute.SCRAP, 1);

        System.out.println(game.ingestEntry(actionEntry).toString(2));

        game.tick();
        System.out.println(game.getState().toString(2));
        System.out.println(game.getState().getBoard().getUnit(new Position(0, 0)).get());
        System.out.println(game.possibleActions(new PlayerRef("TestPlayer")).toString(2));
    }
}