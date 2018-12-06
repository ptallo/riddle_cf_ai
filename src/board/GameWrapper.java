package board;

public class GameWrapper {
    public static void parseInputFromEngine(Game game, String gameBoard) {
        String[] inputBoard = gameBoard.replace("[", "").replace("]", "").split(",");
        for (int i = 0; i < inputBoard.length; i++) {
            int columnNum = i % game.getSettings().getFieldWidth();
            int rowNum = i / game.getSettings().getFieldWidth();
            if (!game.getBoard()[rowNum][columnNum].equals(inputBoard[i])) {
                game.makeMove(game.getSettings().getMyBotId(), columnNum);
            }
        }
    }

    public static void getBotMove(Game game, long timeToFinishBy) {

    }
}
