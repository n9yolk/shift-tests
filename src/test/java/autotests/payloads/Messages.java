package autotests.payloads;

public enum Messages {

    DELETE("Duck is deleted"),
    GOODFLY("I'm flying"),
    GOODSWIM("I’m swimming");

    Messages(String message) {
        this.message = message;
    }

    public String getFullMessage() {
        return message;
    }

    private String message;
}
