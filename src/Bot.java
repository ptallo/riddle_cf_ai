import board.Game;
import board.GameWrapper;
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

            System.err.println("PARSING DATA: " + line);
            System.err.flush();

            if (line.length() == 0) continue;
            String[] parts = line.split(" ");

            if (parts[0].equalsIgnoreCase("settings")) {
                settings.parseSettingsString(parts);
            } else if (parts[0].equalsIgnoreCase("update")) {
                if (game == null) {
                    game = settings.getNewGame();
                }
                if (parts[2].equalsIgnoreCase("round")) {
                    int newRoundNumber = Integer.parseInt(parts[3]);
                    if (game.getRoundNumber() != newRoundNumber || game.getRoundNumber() + 1 != newRoundNumber) {
                        throw new RuntimeException("Rounds out of sync, please stop game!");
                    }
                } else if (parts[2].equalsIgnoreCase("field")) {
                    String boardArray = parts[3];
                    GameWrapper.parseInputFromEngine(game, boardArray);
                }
            } else if (parts[0].equalsIgnoreCase("action")) {
                if (game == null) {
                    game = settings.getNewGame();
                }
                long millisecondsLeft = Long.parseLong(parts[2]);
                GameWrapper.getBotMove(game, System.currentTimeMillis() + millisecondsLeft);
            } else {
                throw new RuntimeException("undefined engine input! Please examin faulty input!");
            }
        }
    }

    public static void main(String[] args) {
        Bot myBot = new Bot();
        myBot.run();
    }
}