package pro.trevor.tankgame.web.server;

import org.json.JSONObject;

import java.io.File;
import java.util.*;

public class Storage {

    private static final String JSON_FILE_SUFFIX = ".json";
    private static final String STATE_FILE_NAME = "game" + JSON_FILE_SUFFIX;
    private static final String STATE_HISTORY_DIRECTORY_NAME = "history";

    private final File base;
    private final Map<UUID, GameInfo> games;

    public Storage(File base) {
        this.base = base.getAbsoluteFile();
        this.games = new HashMap<>();

        boolean success = this.base.mkdirs();
        assert success;
        assert base.exists();
        assert base.isDirectory();
        assert base.canRead();

        loadGamesFromFilesystem();
    }

    public void loadGamesFromFilesystem() {
        File[] files = base.listFiles();
        Log.LOGGER.info("Loading all games in {}\n", base.getAbsolutePath());
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory() && Util.isUUID(file.getName())) {
                loadGameFromDirectory(file);
            }
        }
        Log.LOGGER.info("Finished loading all games in {}\n", base.getAbsolutePath());
    }

    public void loadGameFromDirectory(File game) {
        assert game.isDirectory();
        assert game.exists();
        assert game.canRead();

        Log.LOGGER.info("Loading game with UUID'{}'", game.getName());
        UUID uuid = UUID.fromString(game.getName());

        File gameFile = new File(game, STATE_FILE_NAME);

        assert gameFile.exists();
        assert gameFile.isFile();
        assert gameFile.canRead();

        String gameFileString = Util.readFileToString(gameFile);

        if (gameFileString == null) {
            Log.LOGGER.warn("Failed to load file {}", gameFile.getAbsolutePath());
            return;
        }

        JSONObject gameInfoJson = new JSONObject(gameFileString);
        GameInfo gameInfo = new GameInfo(gameInfoJson);

        assert gameInfo.uuid().equals(uuid);

        Log.LOGGER.debug("Found game '{}' with UUID {}", gameInfo.name(), gameInfo.uuid());

        games.put(uuid, gameInfo);
    }

    public void saveGameToDirectory(GameInfo gameInfo) {
        File file = new File(base, gameInfo.uuid().toString());

        boolean success = file.mkdirs();
        assert success;

        if (file.exists() && !file.isDirectory()) {
            Log.LOGGER.error("File {} exists and is not a directory, skipping save", file.getAbsolutePath());
            return;
        }

        assert file.canWrite();

        File gameFile = new File(file.getAbsolutePath(), STATE_FILE_NAME);

        Log.LOGGER.info("Saving game '{}' with UUID {} to {}", gameInfo.name(), gameInfo.uuid(), gameFile.getAbsolutePath());

        if (!Util.writeStringToFile(gameFile, gameInfo.toJson().toString(2))) {
            Log.LOGGER.warn("Failed to write file {}", gameFile.getAbsolutePath());
        }
    }

    public void saveStateToHistory(GameInfo gameInfo) {
        File file = new File(base, gameInfo.uuid().toString());
        File historyDirectory = new File(file, STATE_HISTORY_DIRECTORY_NAME);

        boolean success = historyDirectory.mkdirs();
        assert success;
        assert historyDirectory.exists();
        assert historyDirectory.isDirectory();
        assert historyDirectory.canWrite();

        File[] historyFiles = Objects.requireNonNull(historyDirectory.listFiles());

        File gameFileToSave = new File(historyDirectory, historyFiles.length + JSON_FILE_SUFFIX);

        Log.LOGGER.info("Saving game '{}' with UUID {} to {}", gameInfo.name(), gameInfo.uuid(), gameFileToSave.getAbsolutePath());

        if (!Util.writeStringToFile(gameFileToSave, gameInfo.game().getState().toJson().toString())) {
            Log.LOGGER.warn("Failed to write file {}", gameFileToSave.getAbsolutePath());
        }
    }

}
