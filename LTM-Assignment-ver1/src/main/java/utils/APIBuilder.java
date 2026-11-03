package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class APIBuilder {

    static { RestAssured.baseURI = ConfigReader.getBaseUrl();  }

    public static Response get(String basePath, Map<String,String> headers) {
        return RestAssured.given()
                            .basePath(basePath)
                            .headers(headers)
                            .redirects().follow(false)
                            .get();
    }

    public static Response post(String basePath, Map<String,String> headers, Object body) {
        return RestAssured.given()
                        .basePath(basePath)
                        .headers(headers)
                        .body(body)
                        .post();
    }

    public static Response put(String basePath, Map<String,String> headers, Object body) {
        return RestAssured.given()
                .basePath(basePath)
                .headers(headers)
                .body(body)
                .put();
    }

    public static Response delete(String basePath, Map<String,String> headers) {
        return RestAssured.given()
                .basePath(basePath)
                .headers(headers)
                .delete();
    }

}
