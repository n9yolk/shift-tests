package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    public void duckUpdate(TestCaseRunner runner, Duck duck) {
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", duck.color())
                .queryParam("height", String.valueOf(duck.height()))
                .queryParam("id", duck.id())
                .queryParam("material", duck.material())
                .queryParam("sound", duck.sound()));
    }

    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    public void validateResponse(TestCaseRunner runner, HttpStatus status, String color, Double height, String material, String sound, String wingsState) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(
                        "{\n" +
                                "  \"color\": \"" + color + "\",\n"
                                + "  \"height\": " + height + ",\n"
                                + "  \"material\": \"" + material + "\",\n"
                                + "  \"sound\": \"" + sound + "\",\n"
                                + "  \"wingsState\": \"" + wingsState
                                + "\"\n" + "}")
        );
    }    public void validateResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ClassPathResource(expectedPayload))
        );
    }    public void validateResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    public void validateResponse(TestCaseRunner runner, String message, HttpStatus status) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(message)
        );
    }

    public void validateResponse(TestCaseRunner runner, HttpStatus status, String id, String color, Double height, String material, String sound, String wingsState) {
                runner.$(http().client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE).body(
                                "{\n" + "  \"id\": \"" + id + "\","
                                        + "  \"color\": \"" + color + "\",\n"
                                        + "  \"height\": " + height + ",\n"
                                        + "  \"material\": \"" + material + "\",\n"
                                        + "  \"sound\": \"" + sound + "\",\n"
                                        + "  \"wingsState\": \"" + wingsState
                                        + "\"\n" + "}")
                );
    }

    public void checkId(TestCaseRunner runner, int coef, Object body){
        runner.$(action -> {
            if(Integer.parseInt(action.getVariable("duckId")) % 2 == coef){
                createDuck(runner, body);
                getId(runner);
            }
        });
    }


    public void getId(TestCaseRunner runner){
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }
}
