package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.Messages;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTests extends DuckActionsClient {
    @Test(description = "Удалить существующую утку; validation with string;")
    @CitrusTest
    public void deleteDuck1(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckDelete(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck is deleted\"" + "\n}", HttpStatus.OK);
    }
    @Test(description = "Удалить существующую утку; validation with resources;")
    @CitrusTest
    public void deleteDuck2(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckDelete(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "Duck/DuckDeleteTests/DuckDeleteTests.json");
    }
    @Test(description = "Удалить существующую утку; validation with payloads;")
    @CitrusTest
    public void deleteDuck3(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckDelete(runner, "${duckId}");
        Message message = new Message().message(Messages.DELETE);
        validateResponse(runner, HttpStatus.OK, message);
    }
}
