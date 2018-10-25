/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package field;

import java.awt.*;
import java.util.ArrayList;

/**
 * Field class
 * 
 * Field class that contains the field status data and various helper functions.
 * 
 * @author Jim van Eeden <jim@riddles.io>
 */

public class Field {
    private final String EMPTY_CELL = ".";

    private String myId;
    private String opponentId;
    private int width;
    private int height;
    private String[][] field;

    public Field() {}

    public Field(Field field) {
        this.myId = field.myId;
        this.opponentId = field.opponentId;
        this.width = field.width;
        this.height = field.height;

        this.field = new String[this.width][this.height];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[x][y] = field.field[x][y];
            }
        }
    }

    /**
     * Initializes and clears field
     * @throws Exception exception
     */
    public void initField() throws Exception {
        try {
            this.field = new String[this.width][this.height];
        } catch (Exception e) {
            throw new Exception("Error: trying to initialize field while field "
                    + "settings have not been parsed yet.");
        }

        clearField();
    }

	/**
	 * Clear the field
	 */
	public void clearField() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.field[x][y] = ".";
            }
        }
	}

    /**
     * Parse field from comma separated String
     * @param s input from engine
     */
    public void parseFromString(String s) {
        clearField();

        String[] split = s.split(",");
        int x = 0;
        int y = 0;

        for (String value : split) {
            this.field[x][y] = value;

            if (++x == this.width) {
                x = 0;
                y++;
            }
        }
    }

    /**
     * Get a list of all valid moves in the current field.
     * @return List that only contains valid moves
     */
    public ArrayList<Point> getValidMoves() {
        ArrayList<Point> validMoves = new ArrayList<>();

        for (int x = 0; x < this.width; x++) {
            int y = getColumnBottomEmptyRow(x);

            if (y >= 0) { // column has an empty row
                validMoves.add(new Point(x, y));
            }
        }

        return validMoves;
    }

    /**
     * Get list of moves that are in a certain column. Will contain exactly
     * one move if the column is not full, an empty list otherwise
     * @param column X coordinate of the column to place the disc in
     * @return List that only contains valid moves
     */
    public ArrayList<Point> getValidMoveInColumn(int column) {
        ArrayList<Point> validMoves = new ArrayList<>();

        int y = getColumnBottomEmptyRow(column);
        if (y >= 0 ) { // Column has an empty row
            validMoves.add(new Point(column, y));
        }

        return validMoves;
    }

    /**
     * Returns whether the opponent gets a play (of given size) out of making this move
     * @param move Move that has been made
     * @param size Size of the opponent row to look for
     * @param multiple Wether to look for multiple rows
     * @return True if the opponent can make given play, false otherwise
     */
    public boolean enablesOpponentPlay(Point move, int size, boolean multiple) {

        if (!isInBounds(move.x, move.y - 1)) return false;

        ArrayList<Point> plays = getPlaysForCoordinate(move.x, move.y - 1, size, this.opponentId);

        if (multiple && plays.size() > 1) return true;

        return plays.size() > 0;
    }

    /**
     * Finds all possible plays to block or get a row of given size. So if you
     * are player 0 and want to find any game winning plays, do findRowPlays(4, "0")
     * for instance.
     * @param size Size of the row to search for
     * @param playerId Id of the row to search for
     * @return All possible plays for given inputs
     */
    public ArrayList<Point> getRowPlays(int size, String playerId) {
        ArrayList<Point> plays = new ArrayList<>();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                if (!this.field[x][y].equals(EMPTY_CELL)) continue;

                // Empty cell below, so can't play there
                if (isInBounds(x, y + 1) && this.field[x][y + 1].equals(EMPTY_CELL)) continue;

                plays.addAll(getPlaysForCoordinate(x, y, size, playerId));
            }
        }

        return plays;
    }

    /**
     * Finds the plays for given coordinate. Multiple of the same points can be returned if
     * there are multiple rows connecting on that same coordinate.
     * @param x X coordinate on the field
     * @param y Y coordinate on the field
     * @param size Size of row to search for
     * @param playerId Player id of the row to search for
     * @return List of plays
     */
    public ArrayList<Point> getPlaysForCoordinate(int x, int y, int size, String playerId) {
        ArrayList<Point> plays = new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = 0; dx <= 1; dx++) {

                if (dx == 0 && dy == 0) continue;

                if (isRowInDirection(x, y, dx, dy, size, playerId)) {
                    plays.add(new Point(x, y));
                }
            }
        }

        return plays;
    }

    /**
     * Finds if there is a row of a certain size in a given direction
     * from a given starting point.
     * @param x Starting x coordinate
     * @param y Staring y coordinate
     * @param dx X direction to search in, can be 0 or 1
     * @param dy Y direction to search in, can be 0 or 1
     * @param size Size the row must be
     * @return True if there is a row, false otherwise
     */
    private boolean isRowInDirection(int x, int y, int dx, int dy, int size, String id) {
        int length = 1;
        int n = 1;
        boolean hasEmptyCell = false;

        while (true) {
            int nextX = x + (n * dx);
            int nextY = y + (n * dy);

            if (isInBounds(nextX, nextY) && this.field[nextX][nextY].equals(id)) {
                length++;
                n = n > 0 ? n + 1 : n - 1;
            } else {
                if (n > 0) {
                    // Check if there's a empty extra cell to get 4 in a row eventually
                    if (isInBounds(nextX, nextY) && this.field[nextX][nextY].equals(EMPTY_CELL)) {
                        hasEmptyCell = true;
                    }

                    n = -1;
                } else {
                    break;
                }
            }
        }

        if (size == 4 && length >= 4) {
            return true;
        }

        if (size == 3 && length == 3 && !hasEmptyCell) {
            return false;
        }

        return size == length;
    }

    /**
     * Finds if the given coordinate is inside the playing field.
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if the coordinate is in bounds, false otherwise.
     */
    private boolean isInBounds(int x, int y) {
        if (x < 0 || x >= this.width) {
            return false;
        }

        if (y < 0 || y >= this.height) {
            return false;
        }

        return true;
    }

    /**
     * Finds the y coordinate of the bottom empty cell in the given column
     * @param x Column coordinate
     * @return Y coordinate of the bottom empty cell
     */
    private int getColumnBottomEmptyRow(int x) {
        for (int y = this.height - 1; y >= 0; y--) {
            if (this.field[x][y].equals(EMPTY_CELL)) {
                return y;
            }
        }

        return -1;
    }

    public void setCellInField(int x, int y, String value) {
        this.field[x][y] = value;
    }

    public void setMyId(String id) {
        this.myId = id;
    }

    public void setOpponentId(String id) {
        this.opponentId = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getMyId() {
        return this.myId;
    }

    public String getOpponentId() {
        return this.opponentId;
    }
}
