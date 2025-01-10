package pro.trevor.tankgame;

import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1 && (args[0].equals("-v") || args[0].equals("--version"))) {
            Main.printVersion();
        } else if (args.length == 0) {
            // TODO
            System.err.println("CLI is not yet implemented");
        } else {
            System.err.println("Expected 0 or 1 arguments:\n    tankgame <-v|--version>");
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
}