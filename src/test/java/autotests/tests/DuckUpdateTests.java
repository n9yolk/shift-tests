package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.CustomMessage;
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

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTests extends DuckActionsClient {
    @Test(description = "Изменить цвет и высоту уточки; validation with string;")
    @CitrusTest
    public void updateColorHeight1(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duck.id("${duckId}").color("black").height(2.0);
        duckUpdate(runner, duck);
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}", HttpStatus.OK);
        validateResponseDB(runner, "${duckId}","black", 2., "rubber", "quack", "FIXED");
    }
    @Test(description = "Изменить цвет и высоту уточки; validation with resources;")
    @CitrusTest
    public void updateColorHeight2(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duck.id("${duckId}").color("black").height(2.0);
        duckUpdate(runner, duck);
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}", HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "Duck/DuckUpdateTests/UpdateColorHeight.json");
    }
    @Test(description = "Изменить цвет и высоту уточки; validation with payloads;")
    @CitrusTest
    public void updateColorHeight3(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duck.id("${duckId}").color("black").height(2.0);
        duckUpdate(runner, duck);
        CustomMessage message = new CustomMessage().message("Duck with id = ${duckId} is updated");
        validateResponse(runner, HttpStatus.OK, message);
        duckProperties(runner, "${duckId}");
        Duck checkDuck = new Duck().color(duck.color()).height(duck.height()).material(duck.material()).sound(duck.sound()).wingsState(duck.wingsState());
        validateResponse(runner, HttpStatus.OK, checkDuck);
    }

    @Test(description = "Изменить цвет и звук уточки")
    @CitrusTest
    public void updateColorSound(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
                duckDeleteDB(runner, "${duckId}")));
        Duck duck = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
        createDuckDB(runner, duck);
        getIdDB(runner);
        duck.id("${duckId}").color("green").height(2.0);
        duckUpdate(runner, duck);
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"" + "\n}", HttpStatus.OK);
        validateResponseDB(runner, "${duckId}", "green", 0.15, "rubber", "meow", "FIXED");
    }
}
