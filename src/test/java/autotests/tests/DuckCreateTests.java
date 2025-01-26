package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckCreateTests extends DuckActionsClient {
    @Test(description = "Утка с material = rubber")
    @CitrusTest
    public void properRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        validateResponse(runner, HttpStatus.OK, "@ignore@", "yellow",0.15, "rubber", "quack", "FIXED");
    }

    @Test(description = "Утка с material = wood")
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        validateResponse(runner, HttpStatus.OK, "@ignore@","yellow", 0.15, "wood", "quack", "FIXED");
    }
}
