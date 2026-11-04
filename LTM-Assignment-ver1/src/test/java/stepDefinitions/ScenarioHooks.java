package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ScenarioHooks {


    @Before
    public void beforeScenario(Scenario scenario) {
        ScenarioContext.setScenario(scenario);
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario != null && scenario.isFailed()) {
                scenario.log("SCENARIO FAILED");
            }
        } finally {
            ScenarioContext.clear();

        }
    }




}