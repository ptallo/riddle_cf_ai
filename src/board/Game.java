package board;

import java.util.ArrayList;

public class Game {

    private int roundNumber;
    private Settings settings;
    private String[][] board;  // [row][column]

    public Game(Settings settings) {
        this.settings = settings;
        board = new String[settings.getFieldHeight()][settings.getFieldWidth()];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ".";
            }
        }
    }

    private Game(Settings settings, int roundNumber, String[][] board) {
        this.settings = settings;
        this.roundNumber = roundNumber;
        this.board = board;
    }

    public Game clone() {
        return new Game(settings, roundNumber, board);
    }

    public void makeMove(BotID id, int column) {
        if (column < 0 || column > board[0].length || !getLegalMoves().contains(column)) {
            throw new RuntimeException("Invalid Column chosen for move!");
        }

        this.roundNumber++;
        for (int i = board.length; i >= 0; i--) {
            if (board[i][column].contains(".")) {
                board[i][column] = id.getId();
                break;
            }
        }
    }

    public ArrayList<Integer> getLegalMoves() {
        ArrayList<Integer> viableColumns = new ArrayList<>();
        for (int i = 0; i < settings.getFieldWidth(); i++) {
            viableColumns.add(i);
        }

        ArrayList<Integer> columnsToRemove = new ArrayList<>();
        for (int column : viableColumns) {
            if (columnIsFull(column)) {
                columnsToRemove.add(column);
            }
        }
        viableColumns.removeAll(columnsToRemove);

        return viableColumns;
    }

    public boolean columnIsFull(int column) {
        return !board[0][column].contains(".");
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Settings getSettings() {
        return settings;
    }

    public String[][] getBoard() {
        return board;
    }
}
