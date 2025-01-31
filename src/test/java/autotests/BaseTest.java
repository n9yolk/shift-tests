package autotests;

import com.consol.citrus.TestCaseRunner;
import autotests.payloads.Duck;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    protected void sendPostRequest(TestCaseRunner runner, HttpClient URL, String path, Object body) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    protected void sendPutRequest(TestCaseRunner runner, HttpClient URL, String path, Duck duck) {
        runner.$(http().client(URL)
                .send()
                .put(path)
                .queryParam("color", duck.color())
                .queryParam("height", String.valueOf(duck.height()))
                .queryParam("id", duck.id())
                .queryParam("material", duck.material())
                .queryParam("sound", duck.sound()));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient URL, String path, String id) {
        runner.$(http().client(URL)
                .send()
                .delete(path)
                .queryParam("id", id));
    }

    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(http()
                .client(URL)
                .send()
                .get(path));
    }

    protected void validationRequest(TestCaseRunner runner, HttpClient URL, HttpStatus status, String color, Double height, String material, String sound, String wingsState) {
        runner.$(http().client(URL)
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
    }

    protected void validationRequest(TestCaseRunner runner, HttpClient URL, HttpStatus status, String expectedPayload) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ClassPathResource(expectedPayload))
        );
    }

    protected void validationRequest(TestCaseRunner runner, HttpClient URL, HttpStatus status, Object expectedPayload) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    protected void validationRequest(TestCaseRunner runner, HttpClient URL, String message, HttpStatus status) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(message)
        );
    }

    protected void validationRequest(TestCaseRunner runner, HttpClient URL, HttpStatus status, String id, String color, Double height, String material, String sound, String wingsState) {
        runner.$(http().client(URL)
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
}