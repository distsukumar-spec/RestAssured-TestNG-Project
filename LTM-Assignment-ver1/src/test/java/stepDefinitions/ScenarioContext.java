package stepDefinitions;

import io.cucumber.java.Scenario;

public class ScenarioContext {

    private static final ThreadLocal<Scenario> SCENARIO = new ThreadLocal<>();

    private ScenarioContext() {
        // utility class
    }

    public static void setScenario(Scenario scenario) {
        if (scenario == null) {
            clear();
            return;
        }
        SCENARIO.set(scenario);
    }

    public static Scenario getScenario() {
        return SCENARIO.get();
    }

    public static void clear() {
        SCENARIO.remove();
    }
}