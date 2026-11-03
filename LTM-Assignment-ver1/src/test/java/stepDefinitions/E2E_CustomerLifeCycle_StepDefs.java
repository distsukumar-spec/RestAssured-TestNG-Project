
package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import org.apache.commons.collections4.map.HashedMap;
import org.testng.Assert;
import pojo.AccountInformation;
import pojo.ContactDetails;
import pojo.CustomerDetails;
import pojo.TransferDetails;
import utils.*;

import java.util.Map;

public class E2E_CustomerLifeCycle_StepDefs extends BaseSteps {

    private Response addCustomer;
    private Response getCustomer;
    private Response getAllCustomers;
    private Response updateCustomer;
    private Response getUpdatedCustomer;
    private Response addAccount;
    private Response getAccount;
    private Response deleteCustomer;
    private Response transactionDetailsForAccount1;
    private Response transactionDetailsForAccount2;
    private Response transferFund;

    private CustomerDetails customerDetails;
    private ContactDetails existingContactDetails;
    private ContactDetails newContactDetails;
    private AccountInformation accountInformation;
    private TransferDetails transferDetails;

    private static long accountNo1 = 0;
    private static long accountNo2 = 0;

    private static Map<String,AccountInformation> runTimeAccts = new HashedMap<String,AccountInformation>();

    @Given("Create New Customer")
    public void createNewCustomer() {

        logStep("Creating a new customer");

        customerDetails = CustomerDetails.getFakeCustomerDetails();

        addCustomer = APIBuilder.post(
                EndPoints.Customer.ADD_CUSTOMER,
                HeaderConfig.getDefaultHeaders(),
                customerDetails
        );

        logApiResponse("CREATE CUSTOMER - POST", addCustomer);

        ResponseValidator.validateResponseCode(
                addCustomer.getStatusCode(), 201
        );

        logSuccess("Customer created successfully");
        logStep("Customer Number: " + customerDetails.getCustomerNumber());
    }


    @When("Retrieve the created customer")
    public void retrieveTheCreatedCustomerProfile() {

        logStep("Retrieving customer details");
        logStep("Customer Number: " + customerDetails.getCustomerNumber());

        getCustomer = APIBuilder.get(
                EndPoints.Customer.getCustomerEndpoint(
                        customerDetails.getCustomerNumber().toString()
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("RETRIEVE CUSTOMER - GET", getCustomer);

        ResponseValidator.validateResponseCode(
                getCustomer.getStatusCode(), 200
        );

        logSuccess("Customer retrieval status validated");

        ResponseValidator.validateJsonPathData(
                getCustomer,
                "customerNumber",
                customerDetails.getCustomerNumber()
        );

        ResponseValidator.validateJsonPathData(
                getCustomer,
                "firstName",
                customerDetails.getFirstName()
        );

        ResponseValidator.validateJsonPathData(
                getCustomer,
                "lastName",
                customerDetails.getLastName()
        );

        logSuccess("Customer number, first name and last name verified");
    }


    @Then("Update the customer contact details")
    public void updateTheCreatedCustomerContactDetails() {

        logStep("Updating customer contact details");

        existingContactDetails = getCustomer.jsonPath()
                .getObject("contactDetails", ContactDetails.class);



        logStep("Existing contact details retrieved successfully "+existingContactDetails);

        newContactDetails = ContactDetails.getFakeContactDetails();

        logStep("New contact details generated");

        customerDetails.setContactDetails(newContactDetails);

        updateCustomer = APIBuilder.put(
                EndPoints.Customer.updateCustomerEndpoint(
                        customerDetails.getCustomerNumber().toString()
                ),
                HeaderConfig.getDefaultHeaders(),
                customerDetails
        );

        logApiResponse("UPDATE CUSTOMER - PUT", updateCustomer);

        ResponseValidator.validateResponseCode(
                updateCustomer.getStatusCode(), 200
        );

        logSuccess("Customer contact details updated successfully");
    }


    @And("Verify the contact details updated")
    public void verifyTheContactDetailsUpdated() {

        logStep("Verifying updated customer contact details");

        getUpdatedCustomer = APIBuilder.get(
                EndPoints.Customer.getCustomerEndpoint(
                        customerDetails.getCustomerNumber().toString()
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("VERIFY UPDATED CUSTOMER - GET", getUpdatedCustomer);

        ResponseValidator.validateResponseCode(
                getUpdatedCustomer.getStatusCode(), 200
        );

        logSuccess("Updated customer retrieval status validated");

        ResponseValidator.validateJsonPathData(
                getUpdatedCustomer,
                "contactDetails.emailId",
                newContactDetails.getEmailId()
        );

        ResponseValidator.validateJsonPathData(
                getUpdatedCustomer,
                "contactDetails.homePhone",
                newContactDetails.getHomePhone()
        );

        ResponseValidator.validateJsonPathData(
                getUpdatedCustomer,
                "contactDetails.workPhone",
                newContactDetails.getWorkPhone()
        );

        logSuccess("Updated email, home phone and work phone verified");
    }

    @Then("Create an Account for the Customer")
    public void createAnAccountForTheCustomer() {

        logStep("Creating a new Account for the Customer");

         accountInformation = AccountInformation.getFakeAccountInformation();

         addAccount = APIBuilder.post(EndPoints.AccountTransactions
                        .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                HeaderConfig.getDefaultHeaders(),
                accountInformation);

        System.out.println("Customer Details: " + customerDetails.getCustomerNumber());
        System.out.println("Account  Details: " + accountInformation);

        ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 201);



    }

    @And("Verify the Account created successfully")
    public void verifyTheAccountCreatedSuccessfully() {
        getAccount = APIBuilder.get(EndPoints.AccountTransactions.getAccountEndpoint(accountInformation.getAccountNumber().toString())
                ,HeaderConfig.getDefaultHeaders());
        System.out.println("Account 1 Details: " + accountInformation.getAccountNumber());
        System.out.println(accountInformation);

        ResponseValidator.validateResponseCode(getAccount, 200,true);
    }

    @Then("Delete the created Customer")
    public void deleteTheCreatedCustomer() {
        deleteCustomer = APIBuilder.delete(EndPoints.Customer
                        .deleteCustomerEndpoint(customerDetails.getCustomerNumber().toString())
                      ,HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(deleteCustomer.getStatusCode(),200);
    }

    @And("Verify the Customer data is deleted")
    public void verifyTheCustomerDataIsDeleted() {

        logStep("Verifying customer details deleted");
        logStep("Customer Number: " + customerDetails.getCustomerNumber());

        getAllCustomers = APIBuilder.get(
                EndPoints.Customer.GET_ALL_CUSTOMERS
                ,HeaderConfig.getDefaultHeaders()

        );

        ResponseValidator.validateResponseCode(getAllCustomers.getStatusCode(), 200);

        getAllCustomers.jsonPath().getList("",CustomerDetails.class).contains(customerDetails);
        ResponseValidator.validateJsonPresence(getAllCustomers.jsonPath().getList(""),customerDetails,false);

        logSuccess("Customer Deletion status validated");
    }

    @And("Verify the Customer associated accounts are deleted")
    public void verifyTheCustomerAssociatedAccountsAreDeleted() {

        logStep("Verifying customer associated accounts deleted");
        logStep("Customer Number: " + customerDetails.getCustomerNumber());

        getAccount = APIBuilder.get(EndPoints.AccountTransactions
                                .getAccountEndpoint(customerDetails.getCustomerNumber().toString())
                                ,HeaderConfig.getDefaultHeaders());

        ResponseValidator.validateResponseCode(getAccount.getStatusCode(),404);

    }

//    @And("Create an Account for the Customer")
    public void createAnAccountForTheCustomer(DataTable dataTable) {
    }

//    @Then("Retrieve the created Account Verify Account Balance {double}")
    public void retrieveTheCreatedAccountVerifyAccountBalance(int arg0, int arg1) {
        
    }

    @When("Transfer {double} from Account {long} to Account {long}")
    public void transferFromAccountToAccount(double transferAmount, long sourceAccountRef,
                                             long destinationAccountRef) {
        long sourceAccount = sourceAccountRef==111111111 ? accountNo1: sourceAccountRef;
        long destinationAccount = destinationAccountRef==222222222 ? accountNo2: destinationAccountRef;

        logStep("Initiating fund transfer");
        logStep("Transfer amount: " + transferAmount);
        logStep("Source account: " + sourceAccount);
        logStep("Destination account: " + destinationAccount);

        transferDetails = TransferDetails.getTransferPayload(
                sourceAccount, destinationAccount, transferAmount
        );
        logStep("Transfer request: " + transferDetails);

        transferFund = APIBuilder.put(
                EndPoints.AccountTransactions.getTransferFundEndpoint(
                        customerDetails.getCustomerNumber().toString()
                ),
                HeaderConfig.getDefaultHeaders(),
                transferDetails
        );

        logApiResponse("FUND TRANSFER - PUT", transferFund);

        /*
         * Do not assert HTTP 200 here. This step is also used by negative
         * scenarios, where the following step validates the expected failure.
         */
        logStep("Fund transfer API execution completed with status: "
                + transferFund.getStatusCode());
    }

//    @Then("Retrieve the created Account {long} and Verify Account Balance {double}")
//    public void retrieveTheCreatedAccountAndVerifyAccountBalance(long accountNo, double accountBalance) {
//
//        accountNo = (accountNo==111111111) ? accountNo1 : accountNo2;
//        getAccount = APIBuilder.get(EndPoints.AccountTransactions.getAccountEndpoint(String.valueOf(accountNo1))
//                ,HeaderConfig.getDefaultHeaders());
//        System.out.println("Account 1 Details: " +accountNo);
//        System.out.println(accountInformation);
//
//        ResponseValidator.validateResponseCode(getAccount, 200,true);
//
//        ResponseValidator.validateJsonPathData(getAccount,"accountBalance",accountBalance);
//
//
//
//    }
    /*
     * Legacy step kept for feature files that use this wording.
     * The DataTable is expected to contain key/value rows.
     */
    // Shared helper for DataTable-based account creation.
    // The feature step "Create new Account" is bound to createNewAccount().

    @Then("Retrieve the created Account {long} and Verify Account Balance {double}")
    public void retrieveTheCreatedAccountVerifyAccountBalance(long account,double expectedBalance) {
        Assert.assertNotNull(accountInformation, "No account has been created yet");
        account = account==111111111 ? accountNo1 : account;
        account = account==222222222 ? accountNo2 : account;

        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(account)
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount, 200, true);
        ResponseValidator.validateJsonPathData(getAccount, "accountBalance", expectedBalance);
        logSuccess("Account balance verified: " + expectedBalance);
    }
    @Then("Verify the {string} Transaction of {double} in Account {int}")
    public void verifyTheTransactionOfInAccount(String transaction, double transactionAmount, long account) {

        long targetAccount = account==111111111 ? accountNo1 : accountNo2;

        transactionDetailsForAccount1 = APIBuilder.get(EndPoints.AccountTransactions
                        .getAllTransactionsEndpoint(String.valueOf(accountNo1)),
                HeaderConfig.getDefaultHeaders());

        ResponseValidator.validateResponseCode(transactionDetailsForAccount1.getStatusCode(), 200);

//        Response transactionDetailsForAccount2 = APIBuilder.get(EndPoints.AccountTransactions
//                        .getAllTransactionsEndpoint(String.valueOf(accountNo2)),
//                HeaderConfig.getDefaultHeaders());
//
//        ResponseValidator.validateResponseCode(transactionDetailsForAccount2.getStatusCode(), 200);
//
//        AppSpecificAssertions.validateTransaction(transferDetails,transferFund,transactionDetailsForAccount1);

        AppSpecificAssertions.validateTransaction(transaction,transactionAmount,targetAccount,transferFund,transactionDetailsForAccount1);
    }

    @Then("Verify Expected API Failure with Status Code {int} and Error Message {string}")
    public void verifyExpectedAPIFailureWithStatusCodeAndErrorMessage(int expStatusCode, String expErrorMsg) {

        logStep("Initiating expected API failure validation");

        int actualStatuCode = transferFund.statusCode();
        String actualResponseBody = transferFund.getBody().asString();

        logStep("Expected HTTP status code: " + expStatusCode);
        logStep("Actual HTTP status code: " + actualStatuCode);

        logApiResponse("FUND TRANSFER - NEGATIVE SCENARIO", transferFund);

        ResponseValidator.validateResponseCode(actualStatuCode, expStatusCode);

        logStep("Successfully validated expected HTTP status code: " + expStatusCode);

        logStep("Expected error message: " + expErrorMsg);

        ResponseValidator.validateResponseBodyContainsData(actualResponseBody, expErrorMsg);

        logStep("Successfully validated expected error message: " + expErrorMsg);

        logStep("Expected API failure validation completed successfully");

    }

    @And("Create new Account")
    public void createNewAccount(DataTable dataTable) {
        Map<String, String> accountDetails =
                dataTable.asMap(String.class, String.class);

        long accountNumber = Long.parseLong(accountDetails.get("AccountNumber"));
        double openingBalance = Double.parseDouble(
                accountDetails.get("openingBalance"));
        String accountType = accountDetails.get("accountType");

        accountInformation = AccountInformation.getFakeAccountInformation();
        accountNo1 = (accountNumber==111111111) ? accountInformation.getAccountNumber() : accountNumber;
        accountNo2 = (accountNumber==222222222) ? accountInformation.getAccountNumber() : accountNumber;
        System.out.println("ac1 "+accountNo1);
        System.out.println("ac2 "+accountNo2);
        accountInformation.setAccountBalance(openingBalance);
        accountInformation.setAccountType(accountType);

        addAccount = APIBuilder.post(EndPoints.AccountTransactions
                        .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                HeaderConfig.getDefaultHeaders(),
                accountInformation);

        System.out.println("Customer Details: " + customerDetails.getCustomerNumber());
        System.out.println("Account  Details: " + accountInformation);

        ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 201);
    }

    @And("Create new Account {string} with {double}")
    public void createNewAccount(String arg0,double initialBalance) {
        logStep("Creating a "+arg0+" Account for the Customer");

        accountInformation = AccountInformation.getFakeAccountInformation();
        runTimeAccts.put(arg0,accountInformation);

        accountInformation.setAccountBalance(initialBalance);
        addAccount = APIBuilder.post(EndPoints.AccountTransactions
                        .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                HeaderConfig.getDefaultHeaders(),
                accountInformation);

        System.out.println("Customer Details: " + customerDetails.getCustomerNumber());
        System.out.println("Account  Details: " + accountInformation);

        ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 201);


    }

    @Then("Retrieve the created Account {string} and Verify Account Balance {double}")
    public void retrieveTheCreatedAccountAndVerifyAccountBalance(String account, double balance) {

        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(account).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount, 200, true);
        ResponseValidator.validateJsonPathData(getAccount, "accountBalance", balance);
        logSuccess("Account balance verified: " + balance);
    }

    @When("Transfer {double} from Account {string} to Account {string}")
    public void transferFromAccountToAccount(double transferAmount, String arg1, String arg2) {

        AccountInformation sourceAccount = runTimeAccts.get(arg1);
        AccountInformation destinationAccount = runTimeAccts.get(arg2);
        System.out.println(transferAmount +" == "+sourceAccount.getAccountBalance());
        System.out.println(transferAmount>0);
        System.out.println(transferAmount<sourceAccount.getAccountBalance());
        boolean postiveAmount = (transferAmount>0) && transferAmount<sourceAccount.getAccountBalance();
        System.out.println(postiveAmount);
        logStep("Initiating fund transfer");
        logStep("Transfer amount: " + transferAmount);
        logStep("Source account: " + runTimeAccts.get(arg1));
        logStep("Destination account: " + runTimeAccts.get(arg2));

        transferDetails = TransferDetails.getTransferPayload(
                sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), transferAmount
        );
        logStep("Transfer request: " + transferDetails);

        transferFund = APIBuilder.put(
                EndPoints.AccountTransactions.getTransferFundEndpoint(
                        customerDetails.getCustomerNumber().toString()
                ),
                HeaderConfig.getDefaultHeaders(),
                transferDetails
        );

        logApiResponse("FUND TRANSFER - PUT", transferFund);

        if(postiveAmount){
            ResponseValidator.validateResponseCode(transferFund.statusCode(),200);
        }else{
            ResponseValidator.validateResponseCode(transferFund.statusCode(), 400);
        }
        /*
         * Do not assert HTTP 200 here. This step is also used by negative
         * scenarios, where the following step validates the expected failure.
         */
        logStep("Fund transfer API execution completed with status: "
                + transferFund.getStatusCode());
    }

//    @Then("Verify the {string} Transaction of {double} in Account {string}")
//    public void verifyTheTransactionOfTransferAmountInAccount(String transaction,double transferAmount,String account) {
//
//        long accountNo = runTimeAccts.get(account);
//        transactionDetailsForAccount1 = APIBuilder.get(EndPoints.AccountTransactions
//                        .getAllTransactionsEndpoint(String.valueOf(accountNo)),
//                HeaderConfig.getDefaultHeaders());
//
//        ResponseValidator.validateResponseCode(transactionDetailsForAccount1.getStatusCode(), 200);
//
//       AppSpecificAssertions.validateTransaction(transaction,transferAmount,accountNo,transferFund,transactionDetailsForAccount1);
//
//
//    }

    @Then("Verify the {string} Transaction of {double} in Account {string}")
    public void verifyTheTransactionOfTransferAmountInAccount(String transaction, double transferAmount, String account) {

        long accountNo = runTimeAccts.get(account).getAccountNumber();

        logStep("Initiating transaction verification");
        logStep("Transaction type: " + transaction);
        logStep("Expected transaction amount: " + transferAmount);
        logStep("Account number: " + accountNo);

        logStep("Fetching all transactions for account: " + accountNo);

        transactionDetailsForAccount1 = APIBuilder.get(
                EndPoints.AccountTransactions
                        .getAllTransactionsEndpoint(String.valueOf(accountNo)),
                HeaderConfig.getDefaultHeaders());

        logApiResponse("GET ACCOUNT TRANSACTIONS", transactionDetailsForAccount1);

        ResponseValidator.validateResponseCode(transactionDetailsForAccount1.getStatusCode(), 200);

        logStep("Transaction API response status validated successfully: "
                + transactionDetailsForAccount1.getStatusCode());

        logStep("Validating " + transaction + " transaction for amount: " + transferAmount);

        AppSpecificAssertions.validateTransaction(
                transaction, transferAmount, accountNo, transferFund, transactionDetailsForAccount1);

        logStep("Successfully verified " + transaction + " transaction of amount "
                + transferAmount + " for account: " + accountNo);

    }

}
