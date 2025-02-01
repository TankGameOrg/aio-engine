package pro.trevor.tankgame;

import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("-v") || args[0].equals("--version")) {
                Main.printVersion();
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
}