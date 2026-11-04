package utils;

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

    public static void validateResponseCode(Response response, int expectedCode,boolean dependencyRequired) {
        int actualCode = response.getStatusCode();
        if (response.statusCode() != expectedCode) {
            if(actualCode>300 && actualCode<400 && response.getBody().jsonPath().getString("redirectUrl")!=null){
                logger.info("Response code Partially accepted. This request requires redirection call with Redirect URL: " + response.getBody().jsonPath().getString("redirectUrl"));
            }else{
                logger.severe("Response doesn't contain redirect URL. Expected response code: " + expectedCode + ", but got: " + actualCode);
                if(dependencyRequired){
                    logger.warning("This api response is required for further api calls. Hence the response considered to be accepted regardless of redirectional status codes.");
                }else {
                    throw new AssertionError("Expected response code: " + expectedCode + ", but got: " + actualCode);
                }
            }
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
        BigDecimal actualValue = BigDecimal.valueOf(response.getBody().jsonPath().getDouble(jsonPath));
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



