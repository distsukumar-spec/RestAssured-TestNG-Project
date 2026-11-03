package stepDefinitions;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;

import io.cucumber.java.Scenario;
import io.restassured.response.Response;

public class BaseSteps {

    protected void logApiResponse(
            String operation,
            Response response) {

        Scenario scenario = ScenarioContext.getScenario();

        scenario.log("========== " + operation + " ==========");
        scenario.log("HTTP Status Code: " + response.getStatusCode());
        scenario.log("Response Time: " + response.getTime() + " ms");

        scenario.attach(
                response.asPrettyString(),
                "application/json",
                operation + " Response"
        );
    }

    protected void logStep(String message) {
        ScenarioContext.getScenario().log("STEP: " + message);
    }

    protected void logSuccess(String message) {
        ScenarioContext.getScenario().log("PASS: " + message);
    }
}