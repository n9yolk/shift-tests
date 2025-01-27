package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(fluent = true)
public class Message {

    @Setter
    private Messages message;

    @JsonProperty("message")
    public String getMessage(){
        return this.message.getFullMessage();
    }
}
