package board;

import search.MinMaxSearch;

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

    public void setRoundNumber(int roundNumber) {
        this.roundNumber++;
        if (this.roundNumber != roundNumber) {
            throw new RuntimeException("Missing rounds please examine input");
        }
    }

    public void updateGameFromEngine(String updateArray) {
        updateArray = updateArray.replace("[", "").replace("]", "");
        String[] items = updateArray.split(",");
        for (int i = 0; i < items.length; i++) {
            Integer fieldWidth = settings.getFieldWidth();
            int row = Math.floorDiv(i, fieldWidth);
            int column = i % fieldWidth;
            board[row][column] = items[i];
        }
    }

    public void updateGameFromBot(int columnNumber) {
        for (int rowNum = board.length - 1; rowNum >= 0; rowNum--) {
            if (board[rowNum][columnNumber].equalsIgnoreCase(".")) {
                board[rowNum][columnNumber] = String.valueOf(settings.getMyBotId());
            }
        }
    }

    public void chooseMove(int startTime, int time) {
        // should sout place_disc i - where i is the number of the column where you wish to make your moves
        MinMaxSearch search = new MinMaxSearch();
        int move = search.search(Math.toIntExact(time - Math.abs(startTime - System.currentTimeMillis())), this);

        System.err.format("place_disc %d", move);
        System.err.flush();

        System.out.println("place_disc " + move);
        System.out.flush();
    }

    public ArrayList<Game> getChildren() {
        ArrayList<Game> children = new ArrayList<>();

        ArrayList<Integer> legalMoves = getLegalMoves();
        for (Integer move : legalMoves) {
            Game child = this.clone();
            child.updateGameFromBot(move);
            children.add(child);
        }

        return children;
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
