package hooks;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.java.Before;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiHooks {


    @BeforeAll
    public static void allureSubThreadParallel() {
        String listenerName = "AllureSelenide";

        if (!(SelenideLogger.hasListener(listenerName)))
            SelenideLogger.addListener(listenerName,
                    new AllureSelenide().
                            screenshots(true).
                            savePageSource(false));
    }
}

