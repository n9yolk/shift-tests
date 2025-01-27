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


public class DuckFlyTests extends DuckActionsClient {
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void flyActive1(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I'm flying\"\n" + "}", HttpStatus.OK);
    }
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void flyActive2(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "Duck/DuckFlyTests/DuckFlyTest.json");
    }
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void flyActive3(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuck(runner, duck);
        getId(runner);
        duckFly(runner, "${duckId}");
        Message message = new Message().message(Messages.GOODFLY);
        validateResponse(runner, HttpStatus.OK, message);
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I can't fly\"\n" + "}", HttpStatus.OK);
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии", priority = 3)
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duck = new DuckProperties().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.UNDEFINED);
        createDuck(runner, duck);
        getId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Wings are not detected :(\"\n" + "}", HttpStatus.OK);
    }
}
