package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckPropertiesTests extends DuckActionsClient {
    @Test(description = "ID - целое нечетное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с material = rubber)")
    @CitrusTest
    public void properRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        getId(runner);
        checkId(runner, 0,"yellow", 0.15, "rubber", "quack", "FIXED");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK,"yellow", 0.15, "rubber", "quack", "FIXED");
    }

    @Test(description = "ID - целое четное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с material = wood)")
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        getId(runner);
        checkId(runner, 1,"yellow", 0.15, "wood", "quack", "FIXED");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "yellow", 0.15, "wood", "quack", "FIXED");
    }
}
