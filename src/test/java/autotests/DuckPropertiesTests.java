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

public class DuckPropertiesTests extends TestNGCitrusSpringSupport{
    @Test(description = "ID - целое нечетное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с materiai = rubber)", priority = 1)
    @CitrusTest
    public void properRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        checkId(runner, 0,"yellow", 0.15, "rubber", "quack", "FIXED");
        duckProp(runner, "${duckId}");
        validateResponse(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
    }

    @Test(description = "ID - целое четное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с materiai = wood)", priority = 2)
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        checkId(runner, 1,"yellow", 0.15, "wood", "quack", "FIXED");
        duckProp(runner, "${duckId}");
        validateResponse(runner, "yellow", 0.15, "wood", "quack", "FIXED");
    }

    public void checkId(TestCaseRunner runner, int coef, String color, double height, String material, String sound, String wingsState){
        runner.$(action -> {
            if(Integer.parseInt(action.getVariable("duckId")) % 2 == coef){
                createDuck(runner, color, height, material, sound, wingsState);
            }
        });
    }
    public void duckProp(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void validateResponse(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
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
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }
}
