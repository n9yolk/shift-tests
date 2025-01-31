package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;



import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsClient extends BaseTest {
    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Создание утки")
    public void createDuck(TestCaseRunner runner, Object body) {
        sendPostRequest(runner, duckService, "/api/duck/create", body);
    }
    @Step("DB; Создание утки")
    public void createDuckDB(TestCaseRunner runner, Duck duck) {
        runner.$(sql(testDb)
                .statement( "INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE)\n" +
                        "values (nextval('hibernate_sequence'),'" +
                        duck.color() + "'," +
                        duck.height() + ",'" +
                        duck.material() + "','" +
                        duck.sound() + "','" +
                        duck.wingsState() + "');")
        );
    }

    @Step("Обновление параметров утки")
    public void duckUpdate(TestCaseRunner runner, Duck duck) {
        sendPutRequest(runner, duckService, "/api/duck/update", duck);
    }

    @Step("Удаление утки")
    public void duckDelete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, duckService, "/api/duck/delete", id);
    }
    @Step("DB; Удаление утки")
    public void duckDeleteDB(TestCaseRunner runner, String id) {
        runner.$(sql(testDb)
                .statement("DELETE FROM DUCK WHERE ID = " + id)
        );
    }

    @Step("Вывод параметров утки")
    public void duckProperties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties?id=" + id);
    }

    @Step("Полет утки")
    public void duckFly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/fly?id=" + id);
    }

    @Step("Кваканье утки")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendGetRequest(runner, duckService, "/api/duck/action/quack?id=" + id +
                       "&repetitionCount=" + repetitionCount +
                        "&soundCount=" + soundCount);
    }

    @Step("Заплыв утки")
    public void duckSwim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/swim?id=" + id);
    }

    @Step("Обработка ответа; параметры переданны String;")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String color, Double height, String material, String sound, String wingsState) {
        validationRequest(runner, duckService, status, color, height, material, sound, wingsState);
    }
    @Step("DB; Обработка ответа; параметры переданны String;")
    public void validateResponseDB(TestCaseRunner runner, String id, String color, Double height, String material, String sound, String wingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM DUCK WHERE ID = " + id)
                .validate("COLOR", color)
                .validate("HEIGHT", height.toString())
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState)
        );
    }
    @Step("Обработка ответа; resources;")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        validationRequest(runner, duckService, status, expectedPayload);
    }
    @Step("Обработка ответа; payloads;")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        validationRequest(runner, duckService, status, expectedPayload);
    }
    @Step("Обработка ответа в виде сообщения;")
    public void validateResponse(TestCaseRunner runner, String message, HttpStatus status) {
        validationRequest(runner, duckService,  message, status);
    }
    @Step("Обработка ответа createDuck")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String id, String color, Double height, String material, String sound, String wingsState) {
        validationRequest(runner, duckService, status, id, color, height, material, sound, wingsState);
    }

    @Step("Проверка id на четность/нечетность")
    public void checkId(TestCaseRunner runner, int coef, Object body){
        runner.$(action -> {
            if(Integer.parseInt(action.getVariable("duckId")) % 2 == coef){
                duckDeleteDB(runner, action.getVariable("duckId"));
                createDuck(runner, body);
                getId(runner);
            }
        });
    }

    @Step("Получение id")
    public void getId(TestCaseRunner runner){
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }
    @Step("DB; Получение id")
    public void getIdDB(TestCaseRunner runner) {
        runner.$(query(testDb)
                .statement( "SELECT MAX(ID) AS DUCKID FROM DUCK")
                .extract("DUCKID", "duckId")
        );
    }

}
