package utils;

import com.google.gson.JsonParser;
import io.restassured.response.Response;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import static utils.AppSpecificAssertions.logger;

public class ResponseValidator {

    static Logger logger = Logger.getLogger(ResponseValidator.class.getName());

    public static void validateResponseCode(int actualCode, int expectedCode) {
        if (actualCode != expectedCode) {
            throw new AssertionError("Expected response code: " + expectedCode + ", but got: " + actualCode);
        }else{
           logger.info("Response code validation passed. Expected and actual code: " + expectedCode);
        }
    }



    public static void validateJsonPathData(Response response,String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        if(actualValue != null) {
            if (!actualValue.toString().equals(expectedValue.toString())) {
                throw new AssertionError("Expected value at JSON path '" + jsonPath + "': " + expectedValue + ", but got: " + actualValue);
            } else {
                logger.info("JSON path data validation passed for path: " + jsonPath + ". Expected and actual value: " + expectedValue);
            }
        }
    }

    public static void validateJsonPathBigDecimal(Response response,String jsonPath, BigDecimal expectedValue) {
        BigDecimal actualValue = JsonParser.parseString(response.getBody().asString()).getAsJsonObject().get(jsonPath).getAsBigDecimal();
            if (actualValue.compareTo(expectedValue)!=0) {
                throw new AssertionError("Expected value at JSON path '" + jsonPath + "': " + expectedValue + ", but got: " + actualValue);
            } else {
                logger.info("JSON path data validation passed for path: " + jsonPath + ". Expected and actual value: " + expectedValue);
            }
        }

    public static void validateResponseBodyContainsData(String respBody, String searchContent) {

            if (!respBody.contains(searchContent)) {
                throw new AssertionError("Response Body '" + respBody + "does not contain the required Content: " + searchContent);
            } else {
                logger.info("Response body has the search Content " + searchContent);
            }

    }

    public static void validateJsonPresence(List<T> jsons, Object obj, boolean shouldBePresent) {

        boolean isPresent = jsons.contains(obj);
        boolean result = isPresent==shouldBePresent;

        if (!result) {
            logger.info(obj.toString() + " is present in the JSON list: " + isPresent);
            throw new AssertionError("JSON presence validation failed. Expected presence: " + shouldBePresent + ", but got: " + isPresent);
        } else {
            logger.info("JSON presence validation passed. Expected presence: " + shouldBePresent + ", Actual presence: " + isPresent);
        }
    }
}



