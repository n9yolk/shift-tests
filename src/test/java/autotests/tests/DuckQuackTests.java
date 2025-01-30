package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.*;
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
@Feature("Эндпоинт /api/duck/action/quack")
public class DuckQuackTests extends DuckActionsClient {
    @Test(description = "Корректный нечётный id, корректный звук; validation with string;")
    @CitrusTest
    public void quackOdd1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack-quack, quack-quack-quack\"" + "}", HttpStatus.OK);
    }
    @Test(description = "Корректный нечётный id, корректный звук; validation with resources;")
    @CitrusTest
    public void quackOdd2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponse(runner, HttpStatus.OK, "Duck/DuckQuackTests/DuckQuackTests.json");
    }
    @Test(description = "Корректный нечётный id, корректный звук; validation with payloads;")
    @CitrusTest
    public void quackOdd3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckQuack(runner, "${duckId}", "2", "3");
        Sound sound = new Sound().sound("quack-quack-quack, quack-quack-quack");
        validateResponse(runner, HttpStatus.OK, sound);
    }

    @Test(description = "Корректный чётный id , корректный звук")
    @CitrusTest
    public void quackEven(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 1, duck);
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack-quack, quack-quack-quack\"" + "}", HttpStatus.OK);
    }
}

