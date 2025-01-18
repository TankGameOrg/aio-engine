package pro.trevor.tankgame.web.server;

public class Environment {

    private static final String STORAGE_PATH = "STORAGE_PATH";

    public static String getStoragePath() {
        return System.getenv().getOrDefault(STORAGE_PATH, "games");
    }

}
