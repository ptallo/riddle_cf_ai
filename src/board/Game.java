package board;

import java.util.ArrayList;
import java.util.Random;

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
        this.roundNumber++;
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
        ArrayList<Integer> moves = getLegalMoves();

        Random random = new Random();
        int moveIndex = random.nextInt(moves.size() - 1);

        System.err.format("place_disc %d", moves.get(moveIndex));
        System.err.flush();

        System.out.println("place_disc " + moves.get(moveIndex));
        System.out.flush();
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
        for (String[] row : board) {
            for (int i = 0; i < row.length; i++) {
                int c = i % row.length;
                if (c == column && row[i].equalsIgnoreCase(".")) {
                    return false;
                }
            }
        }
        return true;
    }
}
