package board;

public class Game {

    private int roundNumber;
    private Settings settings;
    private String[][] board;

    public Game(Settings settings) {
        this.settings = settings;
        board = new String[settings.getFieldHeight()][settings.getFieldWidth()];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ".";
            }
        }
    }

    public void setRoundNumber(int roundNumber) {
        roundNumber++;
        if (this.roundNumber != roundNumber) {
            throw new RuntimeException("Missing rounds please examine input");
        }
    }

    public void updateGame(String updateArray) {
        updateArray = updateArray.replace("[", "").replace("]", "");
        String[] items = updateArray.split(",");
        for (int i = 0; i < items.length; i++) {
            Integer fieldWidth = settings.getFieldWidth();
            int row = Math.floorDiv(i, fieldWidth);
            int column = i % fieldWidth;
            board[row][column] = items[i];
        }
    }

    public void makeMove(int time) {
        // should sout place_disc i - where i is the number of the column where you wish to make your moves
    }
}
