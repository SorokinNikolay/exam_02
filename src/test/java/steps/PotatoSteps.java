package steps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hooks.ApiHooks.getJson;
import static io.restassured.RestAssured.given;
import static utils.PropertiesReader.getFromProperties;

public class PotatoSteps {

    JSONObject baseJO;
    JSONObject responseJO;

    @Когда("^достаём JSONObject из файла (.*)$")
    public void getJO(String fileName) throws IOException {
        getJson(fileName);
        baseJO = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName))));
    }
//
    @Тогда("^назначаем в JSONObject нужные по условиям значения по ключам (.*) и (.*)$")
    public void putJO(String key1, String key2) {
        baseJO.put(key1, getFromProperties("key1"));
        baseJO.put(key2, getFromProperties("key2"));
    }

    @И("^отправляем запрос на (.*) и парсим ответ$")
    public void inOutJO(String url) {

        Response response = given()
                .baseUri(getFromProperties(url))
                .contentType("application/json;charset=UTF-8")
                .when()
                .body(baseJO.toString())
                .post(getFromProperties("postUri"))
                .then()
                .statusCode(201)
                .extract().response();

        responseJO = new JSONObject(response.getBody().asString());
    }

    @И("проверяем, содержит ли вернувшийся JSONObject нужные значения по ключам")
    public void assertJO() {

        Assertions.assertEquals(responseJO.getString("name"),getFromProperties("key1"));
        Assertions.assertEquals(responseJO.getString("job"),getFromProperties("key2"));
        Allure.addAttachment("Результат проверки", "Значения по ключам совпадают");
    }
}
