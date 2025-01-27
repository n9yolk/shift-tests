package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckProperties;
import autotests.payloads.Message;
import autotests.payloads.Messages;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckSwimTests extends DuckActionsClient {

    @Test(description = "Существующий id; validation with string;")
    @CitrusTest
    public void swimExist1(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner,  "{\n" + "  \"message\": \"I’m swimming\"\n" + "}", HttpStatus.OK);
    }
    @Test(description = "Существующий id; validation with resources;")
    @CitrusTest
    public void swimExist2(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner,   HttpStatus.OK, "Duck/DuckSwimTests/DuckGoodSwimTest.json");
    }
    @Test(description = "Существующий id; validation with payloads;")
    @CitrusTest
    public void swimExist3(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckSwim(runner, "${duckId}");
        Message message = new Message().message(Messages.GOODSWIM);
        validateResponse(runner, HttpStatus.OK, message);
    }

    @Test(description = "Несуществующий id")
    @CitrusTest
    public void swimNotExist(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckSwim(runner, "-1");
        validateResponse(runner,  "{\n" + "  \"message\": \"Paws are not found ((((\"\n" + "}", HttpStatus.BAD_REQUEST);
    }
}
