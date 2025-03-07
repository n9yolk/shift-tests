package autotests.tests;

import autotests.clients.DuckActionsClient;
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
@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTests extends DuckActionsClient {
    @Test(description = "ID - целое нечетное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с material = rubber); validation with string;")
    @CitrusTest
    public void properRubber1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK,"yellow", 0.15, "rubber", "quack", "FIXED");
    }
    @Test(description = "ID - целое нечетное число (0, 9223372036854775807)\n" +
            "Есть в БД (утка с material = rubber); validation with resources;")
    @CitrusTest
    public void properRubber2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK,"Duck/Ducks/RubberDuckProperties.json");
    }
    @Test(description = "ID - целое нечетное число (0, 9223372036854775807)\n" +
            "Есть в БД (утка с material = rubber); validation with payloads;")
    @CitrusTest
    public void properRubber3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 0, duck);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, duck);
    }

    @Test(description = "ID - целое четное число (0, 9223372036854775807)\n" +
                        "Есть в БД (утка с material = wood)")
    @CitrusTest
    public void properWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        checkId(runner, 1, duck);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "yellow", 0.15, "wood", "quack", "FIXED");
    }
}
