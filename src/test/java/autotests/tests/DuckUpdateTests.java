package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckUpdateTests extends DuckActionsClient {
    @Test(description = "Изменить цвет и высоту уточки")
    @CitrusTest
    public void updateColorHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        getId(runner);
        duckUpdate(runner, "${duckId}", "black", "2", "rubber", "quack");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}", HttpStatus.OK,"",null,"","","");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "bodyArgs", HttpStatus.OK,"black", 2., "rubber", "quack", "FIXED");
    }

    @Test(description = "Изменить цвет и звук уточки")
    @CitrusTest
    public void updateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        getId(runner);
        duckUpdate(runner, "${duckId}", "green", "0.15", "rubber", "meow");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}", HttpStatus.OK,"",null,"","","");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "bodyArgs", HttpStatus.OK,"green", 0.15, "rubber", "meow", "FIXED");
    }
}
