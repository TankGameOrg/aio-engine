package pro.trevor.tankgame.web.server;

import org.json.JSONArray;
import org.json.JSONObject;
import pro.trevor.tankgame.rule.action.LogEntry;
import pro.trevor.tankgame.state.State;

import java.io.File;
import java.util.*;

public class Storage {

    private static final String JSON_FILE_SUFFIX = ".json";
    private static final String STATE_FILE_NAME = "game" + JSON_FILE_SUFFIX;
    private static final String LOGBOOK_FILE_NAME = "log" + JSON_FILE_SUFFIX;
    private static final String STATE_HISTORY_DIRECTORY_NAME = "history";

    private final File base;
    private final Map<UUID, GameInfo> games;
    private final Map<UUID, List<LogEntry>> logbooks;

    public Storage(File base) {
        this.base = base.getAbsoluteFile();
        this.games = new HashMap<>();
        this.logbooks = new HashMap<>();

        boolean success = this.base.mkdirs();
        assert success;
        assert base.exists();
        assert base.isDirectory();
        assert base.canRead();

        loadGamesFromFilesystem();
    }

    private void initializeGameDirectory(UUID uuid) {
        File gameDirectory = gameDirectory(uuid);
        File historyDirectory = stateHistoryDirectory(uuid);

        gameDirectory.mkdirs();
        assert gameDirectory.exists();
        assert gameDirectory.isDirectory();
        assert gameDirectory.canRead();
        assert gameDirectory.canWrite();

        historyDirectory.mkdir();
        assert historyDirectory.exists();
        assert historyDirectory.isDirectory();
        assert historyDirectory.canRead();
        assert historyDirectory.canWrite();
    }

    private File gameDirectory(UUID uuid) {
        return new File(base, uuid.toString());
    }

    private File stateHistoryDirectory(UUID uuid) {
        return new File(gameDirectory(uuid), STATE_HISTORY_DIRECTORY_NAME);
    }

    private File stateFile(UUID uuid) {
        return new File(gameDirectory(uuid), STATE_FILE_NAME);
    }

    private File logbookFile(UUID uuid) {
        return new File(gameDirectory(uuid), LOGBOOK_FILE_NAME);
    }

    public void loadGamesFromFilesystem() {
        File[] files = base.listFiles();
        Log.LOGGER.info("Loading all games in {}", base.getAbsolutePath());
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory() && Util.isUUID(file.getName())) {
                loadGameFromDirectory(UUID.fromString(file.getName()));
            }
        }
        Log.LOGGER.info("Finished loading all games in {}", base.getAbsolutePath());
    }

    public void loadGameFromDirectory(UUID uuid) {
        Log.LOGGER.info("Loading game with UUID {}", uuid);

        File gameFile = stateFile(uuid);
        JSONObject gameInfoJson = readJsonFromFile(gameFile);

        if (gameInfoJson == null) {
            Log.LOGGER.error("Failed to load game information from {}", gameFile.getAbsolutePath());
            return;
        }

        GameInfo gameInfo = new GameInfo(gameInfoJson);
        assert gameInfo.uuid().equals(uuid);

        File logbookFile = logbookFile(uuid);
        JSONArray logbookJson = readJsonArrayFromFile(logbookFile);

        if (logbookJson == null) {
            Log.LOGGER.error("Failed to load logbook from {}", logbookFile.getAbsolutePath());
            return;
        }

        List<LogEntry> logbook = new ArrayList<>();
        for (int i = 0; i < logbookJson.length(); i++) {
            logbook.add(new LogEntry(logbookJson.getJSONObject(i)));
        }

        Log.LOGGER.debug("Found game '{}' with UUID {}", gameInfo.name(), gameInfo.uuid());

        games.put(uuid, gameInfo);
        logbooks.put(uuid, logbook);
    }

    private void saveGameInfo(GameInfo gameInfo) {
        File file = gameDirectory(gameInfo.uuid());

        if (!file.isDirectory()) {
            Log.LOGGER.error("File {} exists and is not a directory, skipping save", file.getAbsolutePath());
            return;
        }

        File gameFile = new File(file.getAbsolutePath(), STATE_FILE_NAME);

        Log.LOGGER.info("Saving game '{}' with UUID {} to {}", gameInfo.name(), gameInfo.uuid(), gameFile.getAbsolutePath());

        if (!Util.writeStringToFile(gameFile, gameInfo.toJson().toString(2))) {
            Log.LOGGER.error("Failed to write file {}", gameFile.getAbsolutePath());
        }
    }

    private void saveStateToHistory(GameInfo gameInfo) {
        File historyDirectory = stateHistoryDirectory(gameInfo.uuid());

        File[] historyFiles = Objects.requireNonNull(historyDirectory.listFiles());

        File gameFileToSave = new File(historyDirectory, historyFiles.length + JSON_FILE_SUFFIX);

        Log.LOGGER.info("Saving game '{}' with UUID {} to {}", gameInfo.name(), gameInfo.uuid(), gameFileToSave.getAbsolutePath());

        if (!Util.writeStringToFile(gameFileToSave, gameInfo.game().getState().toJson().toString())) {
            Log.LOGGER.error("Failed to write file {}", gameFileToSave.getAbsolutePath());
        }
    }

    private void saveLogbook(GameInfo gameInfo) {
        File logbookFile = logbookFile(gameInfo.uuid());

        Log.LOGGER.info("Saving logbook for game '{}' with UUID {} to {}", gameInfo.name(), gameInfo.uuid(), logbookFile.getAbsolutePath());

        JSONArray logbookJsonArray = new JSONArray();
        for (LogEntry logEntry : logbooks.get(gameInfo.uuid())) {
            logbookJsonArray.put(logEntry.toJson());
        }

        if (!Util.writeStringToFile(logbookFile, logbookJsonArray.toString())) {
            Log.LOGGER.error("Failed to write file {}", logbookFile.getAbsolutePath());
        }
    }

    public void saveGameAfterAction(GameInfo gameInfo, LogEntry logEntry) {
        games.put(gameInfo.uuid(), gameInfo);
        logbooks.get(gameInfo.uuid()).add(logEntry);

        File gameDirectory = gameDirectory(gameInfo.uuid());
        if (!gameDirectory.exists()) {
            initializeGameDirectory(gameInfo.uuid());
        }

        saveStateToHistory(gameInfo);
        saveLogbook(gameInfo);
        saveGameInfo(gameInfo);
    }

    private JSONObject readJsonFromFile(File file) {
        String text = Util.readFileToString(file);

        if (text == null) {
            return null;
        }

        return new JSONObject(text);
    }

    private JSONArray readJsonArrayFromFile(File file) {
        String text = Util.readFileToString(file);

        if (text == null) {
            return null;
        }

        return new JSONArray(text);
    }

    public State readStateFromHistory(UUID uuid, int index) {
        File stateFile = new File(stateHistoryDirectory(uuid), index + JSON_FILE_SUFFIX);
        JSONObject logJson = readJsonFromFile(stateFile);

        if (logJson == null) {
            Log.LOGGER.error("Failed to read history file {}", stateFile.getAbsolutePath());
            return null;
        }

        return new State(logJson);
    }

    public int getGameStateHistorySize(UUID uuid) {
        File[] files = stateHistoryDirectory(uuid).listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    public List<GameInfo> getAllGames() {
        return new ArrayList<>(games.values());
    }

    public GameInfo getGameInfoByUUID(UUID uuid) {
        return games.get(uuid);
    }

    public List<LogEntry> getLogbookByUUID(UUID uuid) {
        return logbooks.get(uuid);
    }

    public Optional<String> getRulesByUUID(UUID uuid) {
        File gameDirectory = gameDirectory(uuid);
        File rulesFile = new File(gameDirectory, "rules.md");
        if (!rulesFile.exists() || !rulesFile.isFile() || !rulesFile.canRead()) {
            return Optional.empty();
        }

        String content = Util.readFileToString(rulesFile);
        if (content == null) {
            return Optional.empty();
        }

        return Optional.of(content);
    }

    public boolean undoAction(UUID uuid) {
        GameInfo gameInfo = games.get(uuid);
        List<LogEntry> logbook = logbooks.get(uuid);

        // Fail if the game information or logbook cannot be found by the given ID
        if (gameInfo == null || logbook == null) {
            Log.LOGGER.error("Attempted to remove most recent action from game '{}' with UUID {}", gameInfo == null ? "null" : gameInfo.name(), uuid);
            return false;
        }

        Log.LOGGER.info("Attempting to remove most recent action from game '{}' with UUID {}", gameInfo.name(), uuid);

        // Succeed if the logbook is already empty
        if (logbook.isEmpty()) {
            Log.LOGGER.error("The logbook is already empty");
            return true;
        }

        File stateToDelete = new File(stateHistoryDirectory(uuid), logbook.size() + JSON_FILE_SUFFIX);

        // Fail if deleting the latest state is not successful
        if (!stateToDelete.delete()) {
            Log.LOGGER.error("Failed to delete state file {}", stateToDelete.getAbsolutePath());
            return false;
        }

        LogEntry removed = logbook.removeLast();
        State newCurrentState = readStateFromHistory(uuid, logbook.size());

        // Fail if we could not read the previous state
        if (newCurrentState == null) {
            Log.LOGGER.error("Failed to read the previous state");
            // Add back the removed entry before returning
            logbook.add(removed);
            return false;
        }

        gameInfo.game().setState(newCurrentState);
        games.put(uuid, gameInfo);
        logbooks.put(uuid, logbook);

        saveLogbook(gameInfo);
        saveGameInfo(gameInfo);

        Log.LOGGER.info("Successfully removed most recent action from game '{}' with UUID {}", gameInfo.name(), gameInfo.uuid());
        return true;
    }

}
