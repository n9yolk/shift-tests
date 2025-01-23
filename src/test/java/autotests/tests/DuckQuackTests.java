package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckQuackTests extends DuckActionsClient {
    @Test(description = "Корректный нечётный id, корректный звук")
    @CitrusTest
    public void quackOdd(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        getId(runner);
        checkId(runner, 0, "yellow", 0.15, "rubber", "quack", "FIXED");
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack-quack, quack-quack-quack\"," + "}", HttpStatus.OK, "",null,"","","");
    }

    @Test(description = "Корректный чётный id , корректный звук")
    @CitrusTest
    public void quackEven(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        getId(runner);
        checkId(runner, 1, "yellow", 0.15, "rubber", "quack", "FIXED");
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack-quack, quack-quack-quack\"," + "}", HttpStatus.OK, "",null,"","","");
    }
}

