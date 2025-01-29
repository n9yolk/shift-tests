package autotests.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {

    DELETE("Duck is deleted"),
    GOODFLY("I'm flying"),
    GOODSWIM("I’m swimming");

    private final String message;
}
