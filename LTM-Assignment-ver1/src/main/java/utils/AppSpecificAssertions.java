package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import pojo.AccountInformation;
import pojo.TransferDetails;

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

    public static void validateTransferSuccessMessage(String ResponseBody,String customerNumber){
        String expectedMessage = "Success: Amount transferred for Customer Number "+customerNumber;
        if(!ResponseBody.contains(expectedMessage)){
            throw new AssertionError("Expected message: " + expectedMessage + ", but got: " + ResponseBody);
        }else{
            logger.info("Transfer success message validation passed. Expected and actual message: " + expectedMessage);
        }
    }

    public static void validateTransaction(String expectedTransaction,double expectedTransactionAmount, long expectedAccountNumber,Response transferResposne,Response transactionResponse) {

//        long expectedAccountNumber = expectedTransaction.equalsIgnoreCase("debit") ? expectedAccountNumber : transferDetails.getToAccountNumber();

        String expectedTransactionTime = transferResposne.headers().get("date").toString().substring(22, 30);
        System.out.println(expectedTransactionTime);

        JsonArray transactions = JsonParser.parseString(transactionResponse.getBody().asString()).getAsJsonArray();
        JsonObject targetTransaction = null;
        for(JsonElement transaction:transactions){
            String actualTransactionType = transaction.getAsJsonObject().get("txType").getAsString();
            String actualTransactionTime = transaction.getAsJsonObject().get("txDateTime").getAsString();
            if(actualTransactionType.equalsIgnoreCase(expectedTransaction) && actualTransactionTime.equalsIgnoreCase(expectedTransactionTime)){
                targetTransaction = transaction.getAsJsonObject();
            }
        }
//        JsonObject actualTransaction = transactionResponse.getBody().jsonPath().getJsonObject(
//                "find { it.txDateTime == '" + expectedTransactionTime +
//                        "' && it.txType == '" + expectedTransaction + "' }"
//        );

        long actualAcctNo = targetTransaction.get("accountNumber").getAsNumber().longValue();
        String actualTxType = targetTransaction.get("txType").getAsString();
        double actualTransferAmount = targetTransaction.get("txAmount").getAsDouble();
        String actualTxDateTime = targetTransaction.get("txDateTime").getAsString();

        if(!(expectedAccountNumber==actualAcctNo
                && expectedTransactionAmount==actualTransferAmount
                && expectedTransactionTime.contentEquals(actualTxDateTime)
                && expectedTransaction.equalsIgnoreCase(actualTxType))){
            throw new AssertionError("Expected Data: " +expectedAccountNumber+" "+expectedTransactionAmount+" "+expectedTransaction
                                            + ", but got: " + targetTransaction.toString());
        }else{
            logger.info("Transaction Details validation passed. Expected and actual message: "
                    +expectedAccountNumber+" "+expectedTransactionAmount+" "+expectedTransaction);
        }

        System.out.println("txdatetime: "+expectedTransactionTime);
        transferResposne.prettyPrint();
        transactionResponse.prettyPrint();
        System.out.println("Expected Transaction: " + expectedTransaction);

    }

    public static void validateCreditDifference(TransferDetails transferDetails,String transactionType, AccountInformation beforeTransaction, AccountInformation afterTransaction) {



    }



}
