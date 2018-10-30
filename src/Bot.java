import board.Game;
import board.Settings;

import java.util.Scanner;

public class Bot {

    private Settings settings;
    private Scanner scan = new Scanner(System.in);
    private Game game;

    public void run() {
        settings = new Settings();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] parts = line.split(" ");
            if (parts[0].equalsIgnoreCase("settings")) {
                settings.parseSettingsString(parts);
                break;
            } else if (parts[0].equalsIgnoreCase("update")) {
                if (game == null) {
                    game = settings.getNewGame();
                }
                if (parts[2].equalsIgnoreCase("round")) {
                    game.setRoundNumber(Integer.parseInt(parts[3]));
                } else if (parts[2].equalsIgnoreCase("field")) {
                    game.updateGame(parts[3]);
                }
            } else if (parts[0].equalsIgnoreCase("action")) {
                if (game == null) {
                    game = settings.getNewGame();
                }
                game.makeMove(Integer.parseInt(parts[2]));
            } else {
                throw new RuntimeException("default statement reached!");
            }
        }
    }

    public static void main(String[] args) {
        (new Bot()).run();
    }
}