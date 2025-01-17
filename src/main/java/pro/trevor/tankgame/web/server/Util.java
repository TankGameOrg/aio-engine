package pro.trevor.tankgame.web.server;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /**
     * Determines whether a given String is a valid UUID.
     * @param uuid the String to check.
     * @return true if the given String is a valid UUID; false otherwise.
     */
    public static boolean isUUID(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches();
    }

    public static String readFileToString(File path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException exception) {
            Log.LOGGER.error(exception.getMessage(), exception);
            return null;
        }
    }

    public static List<String> readFileToLines(File path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            return bufferedReader.lines().toList();
        } catch (IOException exception) {
            Log.LOGGER.error(exception.getMessage(), exception);
            return null;
        }
    }

    public static boolean writeStringToFile(File path, String content) {
        try {
            if (!path.exists()) {
                path.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        } catch (IOException exception) {
            Log.LOGGER.error(exception.getMessage(), exception);
            return false;
        }
    }
}
