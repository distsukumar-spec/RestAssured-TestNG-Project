package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ScenarioHooks {


    @Before
    public void beforeScenario(Scenario scenario) {
        ScenarioContext.setScenario(scenario);
    }






}