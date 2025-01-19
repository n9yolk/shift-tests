package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;

import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreateTests extends TestNGCitrusSpringSupport{
    @Test(description = "Утка с material = rubber", priority = 1)
    @CitrusTest
    public void properRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");

        validateResponse(runner, "{\n" +
                "  \"id\": \"@ignore@\"," +
                "  \"color\": \"yellow\"," +
                "  \"height\": 0.15," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"FIXED\""
                + "}"
        );
    }

    @Test(description = "Утка с material = wood", priority = 2)
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");

        validateResponse(runner, "{\n" +
                "  \"id\": \"@ignore@\"," +
                "  \"color\": \"yellow\"," +
                "  \"height\": 0.15," +
                "  \"material\": \"wood\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"FIXED\""
                + "}"
        );
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }


    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}"));
    }
}
