package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;


import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Создание утки")
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
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
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", duck.color())
                .queryParam("height", String.valueOf(duck.height()))
                .queryParam("id", duck.id())
                .queryParam("material", duck.material())
                .queryParam("sound", duck.sound()));
    }

    @Step("Удаление утки")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }
    @Step("DB; Удаление утки")
    public void duckDeleteDB(TestCaseRunner runner, String id) {
        runner.$(sql(testDb)
                .statement("DELETE FROM DUCK WHERE ID = " + id)
        );
    }

    @Step("Вывод параметров утки")
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    @Step("Полет утки")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    @Step("Кваканье утки")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    @Step("Заплыв утки")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    @Step("Обработка ответа; параметры переданны String;")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String color, Double height, String material, String sound, String wingsState) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(
                        "{\n" +
                                "  \"color\": \"" + color + "\",\n"
                                + "  \"height\": " + height + ",\n"
                                + "  \"material\": \"" + material + "\",\n"
                                + "  \"sound\": \"" + sound + "\",\n"
                                + "  \"wingsState\": \"" + wingsState
                                + "\"\n" + "}")
        );
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
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ClassPathResource(expectedPayload))
        );
    }
    @Step("Обработка ответа; payloads;")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }
    @Step("Обработка ответа в виде сообщения;")
    public void validateResponse(TestCaseRunner runner, String message, HttpStatus status) {
        runner.$(http().client(duckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(message)
        );
    }
    @Step("Обработка ответа createDuck")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String id, String color, Double height, String material, String sound, String wingsState) {
                runner.$(http().client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE).body(
                                "{\n" + "  \"id\": \"" + id + "\","
                                        + "  \"color\": \"" + color + "\",\n"
                                        + "  \"height\": " + height + ",\n"
                                        + "  \"material\": \"" + material + "\",\n"
                                        + "  \"sound\": \"" + sound + "\",\n"
                                        + "  \"wingsState\": \"" + wingsState
                                        + "\"\n" + "}")
                );
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
