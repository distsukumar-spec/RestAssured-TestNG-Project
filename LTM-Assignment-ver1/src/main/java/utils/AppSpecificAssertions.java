package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AppSpecificAssertions {

    static Logger logger = Logger.getLogger(ResponseValidator.class.getName());

    public static void validateJsonSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            logger.info("JSON schema validation passed for schema: " + schemaPath);
        } catch (AssertionError e) {
            logger.severe("JSON schema validation failed for schema: " + schemaPath + ". Error: " + e.getMessage());
            throw e;
        }
    }

    public static void validateTransaction(String expectedTransaction, BigDecimal expectedTransactionAmount, long expectedAccountNumber, Response transferResposne, Response transactionResponse) {

        JsonArray transactions = JsonParser.parseString(transactionResponse.getBody().asString()).getAsJsonArray();
        JsonObject targetTransaction = null;
        for(JsonElement transaction:transactions){
            String actualTransactionType = transaction.getAsJsonObject().get("txType").getAsString();
            BigDecimal actualTransactionAmount = transaction.getAsJsonObject().get("txAmount").getAsBigDecimal();
            if(actualTransactionType.equalsIgnoreCase(expectedTransaction) && actualTransactionAmount.compareTo(expectedTransactionAmount)==0){
                targetTransaction = transaction.getAsJsonObject();
            }
        }

        if(targetTransaction==null){
            throw new AssertionError(("Unable to find the Expected Data: " +expectedAccountNumber+" "+expectedTransactionAmount+" "+expectedTransaction
                    + ", from " + transactions.getAsJsonArray().toString()));
        }
        long actualAcctNo = targetTransaction.get("accountNumber").getAsNumber().longValue();
        String actualTxType = targetTransaction.get("txType").getAsString();
        BigDecimal actualTransferAmount = targetTransaction.get("txAmount").getAsBigDecimal();


        if(!(expectedAccountNumber==actualAcctNo
                && expectedTransactionAmount.compareTo(actualTransferAmount)==0
                && expectedTransaction.equalsIgnoreCase(actualTxType))){
            throw new AssertionError("Expected Data: " +expectedAccountNumber+" "+expectedTransactionAmount+" "+expectedTransaction
                                            + ", but got: " + targetTransaction.toString());
        }else{
            logger.info("Transaction Details validation passed. Expected and actual message: "
                    +expectedAccountNumber+" "+expectedTransactionAmount+" "+expectedTransaction);
        }
    }

}
