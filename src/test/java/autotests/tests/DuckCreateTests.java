package autotests.tests;

import autotests.clients.DuckActionsClient;
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

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTests extends DuckActionsClient {
    @Test(description = "Утка с material = rubber; validation with string;")
    @CitrusTest
    public void properRubber1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getIdDB(runner);
        validateResponse(runner, HttpStatus.OK, "@ignore@", "yellow",0.15, "rubber", "quack", "FIXED");
    }
    @Test(description = "Утка с material = rubber; validation with resources;")
    @CitrusTest
    public void properRubber2(@Optional @CitrusResource TestCaseRunner runner) {
        doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}"));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getIdDB(runner);
        validateResponse(runner, HttpStatus.OK, "Duck/Ducks/RubberDuck.json");
    }
    @Test(description = "Утка с material = rubber; validation with payloads;")
    @CitrusTest
    public void properRubber3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().id("@ignore@").color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getIdDB(runner);
        validateResponse(runner, HttpStatus.OK, duck);
    }

    @Test(description = "Утка с material = wood")
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuck(runner, duck);
        getIdDB(runner);
        validateResponse(runner, HttpStatus.OK, "@ignore@","yellow", 0.15, "wood", "quack", "FIXED");
    }
}
