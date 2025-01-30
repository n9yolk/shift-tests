package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Message;
import autotests.payloads.Messages;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTests extends DuckActionsClient {
    @Test(description = "Существующий id с активными крыльями; validation with string;")
    @CitrusTest
    public void flyActive1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I'm flying\"\n" + "}", HttpStatus.OK);
    }
    @Test(description = "Существующий id с активными крыльями; validation with resources;")
    @CitrusTest
    public void flyActive2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "Duck/DuckFlyTests/DuckFlyTest.json");
    }
    @Test(description = "Существующий id с активными крыльями; validation with payloads;")
    @CitrusTest
    public void flyActive3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckFly(runner, "${duckId}");
        Message message = new Message().message(Messages.GOODFLY);
        validateResponse(runner, HttpStatus.OK, message);
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I can't fly\"\n" + "}", HttpStatus.OK);
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии", priority = 3)
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.UNDEFINED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Wings are not detected\"\n" + "}", HttpStatus.OK);
    }
}
