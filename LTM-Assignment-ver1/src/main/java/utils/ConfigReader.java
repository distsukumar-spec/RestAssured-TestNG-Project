package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "config.properties file not found in test resources");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load config.properties", e);
        }
    }

    public static String getProperty(String key) {

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(
                    "Property '" + key + "' is missing or empty in config.properties");
        }

        return value.trim();
    }

    public static String getEnvironment() {

        // Maven command-line parameter takes priority
        String environment = System.getProperty("env");

        // Fall back to config.properties
        if (environment == null || environment.trim().isEmpty()) {
            environment = getProperty("env");
        }

        return environment.trim().toLowerCase();
    }

    public static String getBaseUrl() {

        String environment = getEnvironment();

        String baseUrl = properties.getProperty(environment);

        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new RuntimeException(
                    "No URL configured for environment: " + environment);
        }

        return baseUrl.trim();
    }
}