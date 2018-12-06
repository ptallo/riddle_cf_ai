package board;

import java.util.Arrays;

public class Settings {
    private Integer timebank;
    private Integer timePerMove;

    private String[] playerNames;
    private String myBotName;
    private BotID myBotId;

    private Integer fieldWidth;
    private Integer fieldHeight;

    public void parseSettingsString(String[] inputArray) {
        if (inputArray[1].equalsIgnoreCase("timebank")) {
            timebank = Integer.parseInt(inputArray[2]);
        } else if (inputArray[1].equalsIgnoreCase("time_per_move")) {
            timePerMove = Integer.parseInt(inputArray[2]);
        } else if (inputArray[1].equalsIgnoreCase("player_names")) {
            String arrayString = inputArray[2].replace("]", "").replace("[", "");
            playerNames = arrayString.split(",");
        } else if (inputArray[1].equalsIgnoreCase("your_bot")) {
            myBotName = inputArray[2];
        } else if (inputArray[1].equalsIgnoreCase("your_botid")) {
            int botId = Integer.parseInt(inputArray[2]);
            if (botId == 0) {
                myBotId = BotID.ZERO;
            } else {
                myBotId = BotID.ONE;
            }
        } else if (inputArray[1].equalsIgnoreCase("field_width")) {
            fieldWidth = Integer.parseInt(inputArray[2]);
        } else if (inputArray[1].equalsIgnoreCase("field_height")) {
            fieldHeight = Integer.parseInt(inputArray[2]);
        }
    }

    public Game getNewGame() {
        if (fieldHeight != null && fieldWidth != null) {
            return new Game(this);
        } else {
            throw new NullPointerException("Field Width or Field Height not initialized!");
        }
    }

    public BotID getMyBotId() {
        return myBotId;
    }

    public Integer getFieldWidth() {
        return fieldWidth;
    }

    public Integer getFieldHeight() {
        return fieldHeight;
    }
}
