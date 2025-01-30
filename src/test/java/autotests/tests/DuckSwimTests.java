package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Message;
import autotests.payloads.Messages;
import autotests.payloads.WingState;
import autotests.payloads.Duck;
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
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTests extends DuckActionsClient {
    @Test(description = "Существующий id; validation with string;")
    @CitrusTest
    public void swimExist1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner,  "{\n" + "  \"message\": \"I’m swimming\"\n" + "}", HttpStatus.OK);
    }
    @Test(description = "Существующий id; validation with resources;")
    @CitrusTest
    public void swimExist2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner,   HttpStatus.OK, "Duck/DuckSwimTests/DuckGoodSwimTest.json");
    }
    @Test(description = "Существующий id; validation with payloads;")
    @CitrusTest
    public void swimExist3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckSwim(runner, "${duckId}");
        Message message = new Message().message(Messages.GOODSWIM);
        validateResponse(runner, HttpStatus.OK, message);
    }

    @Test(description = "Несуществующий id")
    @CitrusTest
    public void swimNotExist(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duckSwim(runner, "-1");
        validateResponse(runner,  "{\n" + "  \"message\": \"Paws are not found ((((\"\n" + "}", HttpStatus.BAD_REQUEST);
    }
}
