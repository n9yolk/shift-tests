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

public class DuckUpdateTests extends TestNGCitrusSpringSupport{
    @Test(description = "Изменить цвет и высоту уточки", priority = 1)
    @CitrusTest
    public void updateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));

        duckUpdate(runner, "${duckId}", "black", "2", "rubber", "quack");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}"
        );
        duckProp(runner, "${duckId}");
        validateResponse(runner, "{\n" +
                "  \"color\": \"black\"," +
                "  \"height\": 2," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"quack\"," +
                "  \"wingsState\": \"FIXED\""
                + "}"
        );
    }

    @Test(description = "Изменить цвет и звук уточки", priority = 2)
    @CitrusTest
    public void updateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));

        duckUpdate(runner, "${duckId}", "green", "0.15", "rubber", "meow");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}"
        );
        duckProp(runner, "${duckId}");
        validateResponse(runner, "{\n" +
                "  \"color\": \"green\"," +
                "  \"height\": 0.15," +
                "  \"material\": \"rubber\"," +
                "  \"sound\": \"meow\"," +
                "  \"wingsState\": \"FIXED\""
                + "}"
        );
    }

    public void duckUpdate(TestCaseRunner runner, String id, String color, String height, String material, String sound) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound));
    }

    public void duckProp(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
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
