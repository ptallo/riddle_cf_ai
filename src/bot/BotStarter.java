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

package bot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;

import field.Field;

/**
 * bot.BotStarter
 * 
 * Magic happens here. You should edit this file, or more specifically
 * the doMove() method to make your bot do more than random moves.
 * 
 * @author Jim van Eeden <jim@riddles.io>
 */

public class BotStarter {

    private final Random RANDOM = new Random();

    private int doCompletelyRandomPrio = 100;
    private int doRandomWithSkipsPrio = 0;
    private int getFourInARowPrio = 0;
    private int getMultipleThreeInARowPrio = 0;
    private int getThreeInARowPrio = 0;
    private int blockFourInARowPrio = 0;
    private int blockMultipleThreeInARowPrio = 0;
    private int blockThreeInARowPrio = 0;
    private int getLeftSideColumnPrio = 0;
    private int getXis1ColumnPrio = 0;
    private int getXis2ColumnPrio = 0;
    private int getCenterColumnPrio = 0;
    private int getXis4ColumnPrio = 0;
    private int getXis5ColumnPrio = 0;
    private int getRightSideColumnPrio = 0;
    private int skipOnOpponentFourInARowPrio = 0;
    private int skipOnOpponentMultipleThreeInARowPrio = 0;
    private int skipOnOpponentThreeInARowPrio = 0;

    private BotStarter() {
        System.err.println("Priorities (higher numbers go first):");
        System.err.println("doCompletelyRandom: " + this.doCompletelyRandomPrio);
        System.err.println("doRandomWithSkips: " + this.doRandomWithSkipsPrio);
        System.err.println("getFourInARow: " + this.getFourInARowPrio);
        System.err.println("getMultipleThreeInARow: " + this.getMultipleThreeInARowPrio);
        System.err.println("getThreeInARow: " + this.getThreeInARowPrio);
        System.err.println("blockFourInARow: " + this.blockFourInARowPrio);
        System.err.println("blockMultipleThreeInARow: " + this.blockMultipleThreeInARowPrio);
        System.err.println("blockThreeInARow: " + this.blockThreeInARowPrio);
        System.err.println("getLeftSideColumn: " + this.getLeftSideColumnPrio);
        System.err.println("getXIs1Column: " + this.getXis1ColumnPrio);
        System.err.println("getXIs2Column: " + this.getXis2ColumnPrio);
        System.err.println("getCenterColumn: " + this.getCenterColumnPrio);
        System.err.println("getXis4Column: " + this.getXis4ColumnPrio);
        System.err.println("getXis5Column: " + this.getXis5ColumnPrio);
        System.err.println("getRightSideColumn: " + this.getRightSideColumnPrio);
        System.err.println("skipOnOpponentFourInARow: " + this.skipOnOpponentFourInARowPrio);
        System.err.println("skipOnOpponentMultipleThreeInARow: " + this.skipOnOpponentMultipleThreeInARowPrio);
        System.err.println("skipOnOpponentThreeInARow: " + this.skipOnOpponentThreeInARowPrio);
    }

    /**
    * Makes a turn. Edit this method to make your bot smarter.
    * @return The column where the turn was made.
    */
    public int doMove(BotState state) {
        Field field = state.getField();

        System.err.println("Roud: " + state.getRoundNumber());

        HashMap<Integer, ArrayList<Callable<ArrayList<Point>>>> priorityMap = initializePriorityMap(field);

        ArrayList<Integer> sortedKeys = new ArrayList<>(priorityMap.keySet());
        sortedKeys.sort(Collections.reverseOrder());

        while (!sortedKeys.isEmpty()) {
            int priority = sortedKeys.remove(0);

            if (priority == this.doCompletelyRandomPrio) {
                break;
            }

            ArrayList<Callable<ArrayList<Point>>> methodList = priorityMap.get(priority);
            Collections.shuffle(methodList);

            while (!methodList.isEmpty()) {
                Callable<ArrayList<Point>> method = methodList.remove(0);

                // Call the method that now has the top priority
                try {
                    ArrayList<Point> moves = method.call();

                    // Skip moves if needed
                    for (Point move : moves) {
                        Field nextField = new Field(field);
                        nextField.setCellInField(move.x, move.y, field.getMyId());

                        if (priority < this.skipOnOpponentFourInARowPrio) {
                            if (nextField.enablesOpponentPlay(move, 4, false)) {
                                System.err.println("skipOnOpponentFourInARow");
                                continue;
                            }
                        }

                        if (priority < this.skipOnOpponentMultipleThreeInARowPrio) {
                            if (nextField.enablesOpponentPlay(move, 3, true)) {
                                System.err.println("skipOnOpponentMultipleThreeInARow");
                                continue;
                            }
                        }

                        if (priority < this.skipOnOpponentThreeInARowPrio) {
                            if (nextField.enablesOpponentPlay(move, 3, false)) {
                                System.err.println("skipOnOpponentThreeInARow");
                                continue;
                            }
                        }

                        return move.x;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // doCompletelyRandom
        return doRandom(field).get(0).x; // There should always be at least one valid move
    }

    private HashMap<Integer, ArrayList<Callable<ArrayList<Point>>>> initializePriorityMap(Field field) {
        HashMap<Integer, ArrayList<Callable<ArrayList<Point>>>> priorityMap = new HashMap<>();

        priorityMap.putIfAbsent(this.doRandomWithSkipsPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getFourInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getMultipleThreeInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getThreeInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.blockFourInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.blockMultipleThreeInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.blockThreeInARowPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getLeftSideColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getXis1ColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getXis2ColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getCenterColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getXis4ColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getXis5ColumnPrio, new ArrayList<>());
        priorityMap.putIfAbsent(this.getRightSideColumnPrio, new ArrayList<>());

        priorityMap.get(this.doRandomWithSkipsPrio).add(() -> doRandom(field));
        priorityMap.get(this.getFourInARowPrio).add(() -> fourInARow(field, field.getMyId()));
        priorityMap.get(this.getMultipleThreeInARowPrio).add(() -> multipleThreeInARow(field, field.getMyId()));
        priorityMap.get(this.getThreeInARowPrio).add(() -> threeInARow(field, field.getMyId()));
        priorityMap.get(this.blockFourInARowPrio).add(() -> fourInARow(field, field.getOpponentId()));
        priorityMap.get(this.blockMultipleThreeInARowPrio).add(() -> multipleThreeInARow(field, field.getOpponentId()));
        priorityMap.get(this.blockThreeInARowPrio).add(() -> threeInARow(field, field.getOpponentId()));
        priorityMap.get(this.getLeftSideColumnPrio).add(() -> placeInColumn(field, 0));
        priorityMap.get(this.getXis1ColumnPrio).add(() -> placeInColumn(field, 1));
        priorityMap.get(this.getXis2ColumnPrio).add(() -> placeInColumn(field, 2));
        priorityMap.get(this.getCenterColumnPrio).add(() -> placeInColumn(field, 3));
        priorityMap.get(this.getXis4ColumnPrio).add(() -> placeInColumn(field, 4));
        priorityMap.get(this.getXis5ColumnPrio).add(() -> placeInColumn(field, 5));
        priorityMap.get(this.getRightSideColumnPrio).add(() -> placeInColumn(field, 6));

        return priorityMap;
    }

    private ArrayList<Point> doRandom(Field field) {
        ArrayList<Point> validMoves = field.getValidMoves();

        Collections.shuffle(validMoves, RANDOM);

        System.err.println("doRandom");

        return validMoves;
    }

    private ArrayList<Point> fourInARow(Field field, String id) {
        ArrayList<Point> plays = field.getRowPlays(4, id);

        if (plays.size() <= 0) {
            return new ArrayList<>();
        }

        if (id.equals(field.getMyId())) {
            System.err.println("getFourInARow");
        } else {
            System.err.println("blockFourInARow");
        }

        return plays;
    }

    private ArrayList<Point> multipleThreeInARow(Field field, String id) {
        ArrayList<Point> plays = field.getRowPlays(3, id);

        for (int i = 0; i < plays.size(); i++) {
            for (int j = i + 1; j < plays.size(); j++) {

                // Two of the same plays means we get two rows of 3 in one move
                if (plays.get(i).equals(plays.get(j))) {
                    if (id.equals(field.getMyId())) {
                        System.err.println("getMultipleThreeInARow");
                    } else {
                        System.err.println("blockMultipleThreeInARow");
                    }

                    return plays;
                }
            }
        }

        return new ArrayList<>();
    }

    private ArrayList<Point> threeInARow(Field field, String id) {
        ArrayList<Point> plays = field.getRowPlays(3, id);

        if (plays.size() <= 0) {
            return new ArrayList<>();
        }

        if (id.equals(field.getMyId())) {
            System.err.println("getThreeInARow");
        } else {
            System.err.println("blockThreeInARow");
        }

        return plays;
    }

    private ArrayList<Point> placeInColumn(Field field, int column) {
        ArrayList<Point> validMoves = field.getValidMoveInColumn(column);

        System.err.println("getColumnXis" + column);

        return validMoves;
    }
     
 	public static void main(String[] args) {
 		BotParser parser = new BotParser(new BotStarter());
 		parser.run();
 	}
 }
