package steps;


import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class MortySteps {

    JSONObject mortyJson;
    JSONObject episodeJson;
    JSONObject characterJson;

    @Когда("получаем информацию о Морти")
    public void getMorty() {
        Response response1 = given()
                .baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/2")
                .then()
                .statusCode(200)
                .extract().response();

        mortyJson = new JSONObject(response1.getBody().asString());
    }

    @Тогда("узнаём последний эпизод, в котором был Морти и запрашиваем информацию о нём")
    public void getEpisode() {
        JSONArray episodeArrayWithMorty = mortyJson.getJSONArray("episode");
        int episodeCountWithMorty = episodeArrayWithMorty.length();

        String lastEpisode = episodeArrayWithMorty.getString(episodeCountWithMorty - 1);
        Allure.addAttachment("Последний эпизод в котором был Морти Смит",
                lastEpisode.substring(lastEpisode.length() - 2));

        Response response2 = given()
                .contentType(ContentType.JSON)
                .get(lastEpisode)
                .then().extract().response();

        episodeJson = new JSONObject(response2.getBody().asString());
    }

    @И("узнаём последнего персонажа из эпизода и запрашиваем о нём информацию")
    public void getCharacter() {

        JSONArray allCharactersInLastEpisode = episodeJson.getJSONArray("characters");
        int charactersCount = allCharactersInLastEpisode.length();

        String lastCharacter = allCharactersInLastEpisode.getString(charactersCount - 1);

        Response response3 = given()
                .contentType(ContentType.JSON)
                .get(lastCharacter)
                .then().extract().response();

        characterJson = new JSONObject(response3.getBody().asString());
    }

    @И("сраниваем рассы и локации Морти и последнего персонажа")
    public void compare() {

        String characterName = characterJson.getString("name");
        String characterSpecies = characterJson.getString("species");
        String characterLocation = characterJson.getJSONObject("location").getString("name");
        String CharacterInfo =
                "\nПоследний персонаж в последнем эпизоде — " + characterName
                        + "\nЕго раса — " + characterSpecies
                        + "\nЕго местонахождение — " + characterLocation;

        Allure.addAttachment("Информация о персонаже", CharacterInfo);

        String mortyName = mortyJson.getString("name");
        String mortySpecies = mortyJson.getString("species");
        String mortyLocation = mortyJson.getJSONObject("location").getString("name");
        String mortyInfo =
                "\nПолное имя Морти — " + mortyName
                        + "\nЕго раса — " + mortySpecies
                        + "\nЕго местонахождение — " + mortyLocation;

        Allure.addAttachment("Информация о Морти", mortyInfo);

        assertEquals(mortySpecies,characterSpecies);
        assertNotEquals(mortyLocation, characterLocation);

        String compareResult = "Расы " + mortyName + " и " + characterName + " совпадают"
                + "\nЛокации " + mortyName + " и " + characterName + " не совпадают";
        Allure.addAttachment("Результат сравнения", compareResult);
    }
}
