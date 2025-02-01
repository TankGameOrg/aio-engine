package pro.trevor.tankgame.web.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import pro.trevor.tankgame.Game;
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

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class Server {

    private final Storage storage;

    public Server() {
        this.storage = new Storage(new File(Environment.getStoragePath()));
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/games/list")
    public String gamesList() {
        List<GameInfo> games = storage.getAllGames();

        JSONArray gamesJson = new JSONArray();
        for (GameInfo gameInfo : games) {
            JSONObject gameInfoJson = gameInfo.toJsonWithoutState();
            gameInfoJson.put("running", gameInfo.game().getState().getOrElse(Attribute.RUNNING, true));
            gamesJson.put(gameInfoJson);
        }

        return gamesJson.toString();
    }

    @GetMapping("/game/{uuid}")
    public String getGame(@PathVariable(name = "uuid") String uuidString) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);
        List<LogEntry> logbook = storage.getLogbookByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        JSONObject gameInfoJson = gameInfo.toJsonWithoutState();

        JSONArray logbookJson = new JSONArray();
        for (LogEntry logEntry : logbook) {
            logbookJson.put(logEntry.toJson());
        }

        gameInfoJson.put("logbook", logbookJson);

        return gameInfoJson.toString();
    }

    @PostMapping("/game/{uuid}/validate")
    public String postUserValidateActionParameters(@PathVariable(name = "uuid") String uuidString, @RequestBody String requestBody) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        LogEntry logEntry = new LogEntry(new JSONObject(requestBody));

        return gameInfo.game().checkActionConditions(logEntry).toString();
    }

    @PostMapping("/game/{uuid}/action")
    public String postGameAction(@PathVariable(name = "uuid") String uuidString, @RequestBody String requestBody) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        LogEntry logEntry = new LogEntry(new JSONObject(requestBody));
        JSONObject output = gameInfo.game().ingestEntry(logEntry);
        if (!output.getBoolean("error")) {
            storage.saveGameAfterAction(gameInfo, logEntry);
        }

        return output.toString();
    }

    @PostMapping("/game/{uuid}/undo")
    public String postRedactGameAction(@PathVariable(name = "uuid") String uuidString) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        if (!storage.undoAction(uuid)) {
            error(new JSONObject().put("message", "Failed to undo most recent action"));
        }

        return success(new JSONObject());
    }

    @PostMapping("/game/{uuid}/tick")
    public String postGameTick(@PathVariable(name = "uuid") String uuidString) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        LogEntry logEntry = new LogEntry(Map.of(Attribute.TICK, gameInfo.game().getState().getOrElse(Attribute.TICK, 0) + 1));
        JSONObject output = gameInfo.game().ingestEntry(logEntry);
        if (!output.getBoolean("error")) {
            storage.saveGameAfterAction(gameInfo, logEntry);
        }

        return output.toString();
    }

    @GetMapping("/game/{uuid}/actions/{player}")
    public String getUserPossibleActions(@PathVariable(name = "uuid") String uuidString, @PathVariable(name = "player") String player) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        player = URLDecoder.decode(player, StandardCharsets.UTF_8);

        Optional<Player> maybePlayer = gameInfo.game().getState().getPlayer(new PlayerRef(player));
        if (maybePlayer.isEmpty()) {
            return error(new JSONObject().put("message", "Invalid player"));
        }

        return gameInfo.game().possibleActions(new PlayerRef(player)).toString();
    }

    @GetMapping("/game/{uuid}/state/current")
    public String getGameCurrentStateNumber(@PathVariable(name = "uuid") String uuidString) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        int states = storage.getGameStateHistorySize(uuid);

        return String.valueOf(states);
    }

    @GetMapping("/game/{uuid}/state/{id}")
    public String getGamePreviousState(@PathVariable(name = "uuid") String uuidString, @PathVariable(name = "id") int id) {
        if (!Util.isUUID(uuidString)) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        UUID uuid = UUID.fromString(uuidString);
        GameInfo gameInfo = storage.getGameInfoByUUID(uuid);

        if (gameInfo == null) {
            return error(new JSONObject().put("message", "Invalid game UUID"));
        }

        int states = storage.getGameStateHistorySize(uuid);

        if (id >= states) {
            return error(new JSONObject().put("message", "Invalid game state ID"));
        }

        return storage.readStateFromHistory(uuid, id).toJson().toString();
    }

    public static String success(JSONObject data) {
        return data.put("error", false).toString();
    }

    public static String error(JSONObject data) {
        return data.put("error", true).toString();
    }

}
