package board;

public enum BotID {
    ZERO("0"),
    ONE("1");

    BotID(String id) {
        this.id = id;
    }

    private String id;

    public String getId() {
        return id;
    }
}
