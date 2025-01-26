package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckSwimTests extends DuckActionsClient {

    @Test(description = "Существующий id")
    @CitrusTest
    public void swimExist(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        getId(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner,  "{\n" + "  \"message\": \"I’m swimming\"\n" + "}", HttpStatus.OK);
    }

    @Test(description = "Несуществующий id")
    @CitrusTest
    public void swimNotExist(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        getId(runner);
        duckSwim(runner, "-1");
        validateResponse(runner,  "{\n" + "  \"message\": \"Paws are not found ((((\"\n" + "}", HttpStatus.BAD_REQUEST);
    }
}
