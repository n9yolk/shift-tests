package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckFlyTests extends DuckActionsClient {
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I'm flying\"\n" + "}", HttpStatus.OK, "",null,"","","");
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I can't fly\"\n" + "}", HttpStatus.OK, "",null,"","","");
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии", priority = 3)
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "UNDEFINED");
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Wings are not detected :(\"\n" + "}", HttpStatus.OK, "",null,"","","");
    }
}
