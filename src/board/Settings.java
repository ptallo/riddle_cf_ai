package board;

public class Settings {
    private Integer timebank;
    private Integer timePerMove;

    private String[] playerNames;
    private String myBotName;
    private Integer myBotId;

    private Integer fieldWidth;
    private Integer fieldHeight;

    public void parseSettingsString(String[] inputArray) {
        switch (inputArray[1]) {
            case "timebank" : {
                timebank = Integer.parseInt(inputArray[2]);
            }
            case "time_per_move" : {
                timePerMove = Integer.parseInt(inputArray[2]);
            }
            case "player_names" : {
                String arrayString = inputArray[2].replace("]", "").replace("[", "");
                playerNames = arrayString.split(",");
            }
            case "your_bot" : {
                myBotName = inputArray[2];
            }
            case "your_botid" : {
                myBotId = Integer.parseInt(inputArray[2]);
            }
            case "field_width" : {
                fieldWidth = Integer.parseInt(inputArray[2]);
            }
            case "field_height" : {
                fieldHeight = Integer.parseInt(inputArray[2]);
            }
        }
    }

    public Game getNewGame() {
        if (fieldHeight != null && fieldWidth != null) {
            return new Game(this);
        } else {
            throw new NullPointerException("Field Width or Field Height not initialized!");
        }
    }

    public Integer getMyBotId() {
        return myBotId;
    }

    public Integer getFieldWidth() {
        return fieldWidth;
    }

    public Integer getFieldHeight() {
        return fieldHeight;
    }
}
