
package stepDefinitions;

import com.google.gson.JsonParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class E2E_CustomerLifeCycle_StepDefs extends BaseSteps {

    protected Response addCustomer;
    protected Response getCustomer;
    protected Response getAllCustomers;
    protected Response updateCustomer;
    protected Response getUpdatedCustomer;
    protected Response addAccount;
    protected Response getAccount;
    protected Response deleteCustomer;
    protected Response transactionDetailsForAccount1;
    protected Response transferFund;

    protected CustomerDetails customerDetails;
    protected ContactDetails existingContactDetails;
    protected ContactDetails newContactDetails;
    protected AccountInformation accountInformation;
    protected TransferDetails transferDetails;

    protected static long accountNo1 = 0;
    protected static long accountNo2 = 0;

    protected static Map<String,AccountInformation> runTimeAccts = new HashedMap<String,AccountInformation>();
    protected static Map<AccountInformation,Integer> runTimeTranHistory = new HashedMap<AccountInformation,Integer>();
    protected static Response currentResponse = null;


    @Given("Create New Customer")
    public void createNewCustomer(DataTable dataTable) {
        Map<String, String> customerData = dataTable.asMap();

        String firstName = customerData.get("firstName");
        String lastName = customerData.get("lastName");

        logStep("Creating a new customer");

        customerDetails = CustomerDetails.getFakeCustomerDetails();
        customerDetails.setFirstName(firstName);
        customerDetails.setLastName(lastName);

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
    }

    @When("attempting to create a same customer instance")
    public void attemptingToCreateASameCustomerInstance() {
        logStep("Attempting to create a same customer instance");
        logStep(customerDetails.toString());

        addCustomer = APIBuilder.post(
                EndPoints.Customer.ADD_CUSTOMER,
                HeaderConfig.getDefaultHeaders(),
                customerDetails
        );

        currentResponse = addCustomer;

        logApiResponse("CREATE CUSTOMER - POST", addCustomer);


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
    public void updateTheCreatedCustomerContactDetails(DataTable dataTable) {
        Map<String, String> contactDetails = dataTable.asMaps().get(0);
        String email = contactDetails.get("email");
        String workPhone = String.valueOf(contactDetails.get("work"));
        String homePhone = String.valueOf(contactDetails.get("home"));

        logStep("Updating customer contact details");

        existingContactDetails = getCustomer.jsonPath()
                .getObject("contactDetails", ContactDetails.class);



        logStep("Existing contact details retrieved successfully "+existingContactDetails);

        newContactDetails = ContactDetails.getFakeContactDetails();
        newContactDetails.setEmailId(email);
        newContactDetails.setWorkPhone(workPhone);
        newContactDetails.setHomePhone(homePhone);

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

    @Then("Create below Accounts for the Customer")
    public void createBelowAccountsForTheCustomer(DataTable dataTable) {

        List<Map<String, String>> accounts = dataTable.asMaps(
                String.class, String.class
        );

        for (Map<String, String> account : accounts) {

            String accountIdentifier = account.get("account");
            String accountType = account.get("accountType");
            BigDecimal initialBalance = new BigDecimal(account.get("initialBalance"));

            logStep("Creating a new Account for the Customer");

            accountInformation = AccountInformation.getFakeActiveAccountInformation();
            accountInformation.setAccountType(accountType);
            accountInformation.setAccountBalance(initialBalance);
            logStep("Payload: "+accountInformation+"for Customer "+customerDetails.getCustomerNumber().toString());
            runTimeAccts.put(accountIdentifier,accountInformation);

            addAccount = APIBuilder.post(EndPoints.AccountTransactions
                            .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                    HeaderConfig.getDefaultHeaders(),
                    accountInformation);

            ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 201);


        }

    }



    @When("attempting to create a duplicate account")
    public void attemptingToCreateADuplicateAccount() {
            logStep("Attempting to create a duplicate Account for the Customer");
            addAccount = APIBuilder.post(EndPoints.AccountTransactions
                            .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                    HeaderConfig.getDefaultHeaders(),
                    accountInformation);
            currentResponse = addAccount;
            ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 400);
    }

    @And("Verify the Accounts created successfully")
    public void verifyTheAccountsCreatedSuccessfully() {
        for(Map.Entry<String,AccountInformation> accounts:runTimeAccts.entrySet()){
            AccountInformation account = accounts.getValue();
           String accountNumber = accounts.getValue().getAccountNumber().toString();
            getAccount = APIBuilder.get(EndPoints.AccountTransactions.getAccountEndpoint(accountNumber)
                    ,HeaderConfig.getDefaultHeaders());
            ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);

            logSuccess("Verify Created Account: "+accountNumber
                                +" Account Type: "+account.getAccountType()
                                +" Account Balance: "+account.getAccountBalance());

            ResponseValidator.validateJsonPathData(
                    getAccount,
                    "accountNumber",
                    account.getAccountNumber()
            );

            ResponseValidator.validateJsonPathData(
                    getAccount,
                    "accountType",
                    account.getAccountType()
            );

            ResponseValidator.validateJsonPathBigDecimal(
                    getAccount,
                    "accountBalance",
                    account.getAccountBalance()
            );

            logSuccess("Account Number, Account Type and Account Balance verified");

        }

    }

    @Then("Delete the created Customer")
    public void deleteTheCreatedCustomer() {
        logStep("Deleting Customer - "+customerDetails.getCustomerNumber());

        deleteCustomer = APIBuilder.delete(EndPoints.Customer
                        .deleteCustomerEndpoint(customerDetails.getCustomerNumber().toString())
                      ,HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(deleteCustomer.getStatusCode(),200);
        logSuccess("User Deleted Successfully and Received API Response: "+deleteCustomer.getStatusCode());

        logApiResponse("Delete Customer: "+customerDetails.getCustomerNumber(),deleteCustomer);
    }

    @And("Verify the Customer Profile is unable to retrieve")
    public void verifyTheCustomerProfileIsUnableToRetrieve() {
        getCustomer = APIBuilder.get(EndPoints.Customer
                        .getCustomerEndpoint(customerDetails.getCustomerNumber().toString())
                ,HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(getCustomer.getStatusCode(),404);

    }

    @And("Verify the respective errorMsg received on retrieving a deleted customer")
    public void verifyTheErrorMsgReceivedOnRetrievingADeletedCustomer(DataTable dataTable) {
        String errorMsg = dataTable.asMaps(String.class, String.class)
                                    .get(0)
                                    .get("errorMsg");

        String respBody = getCustomer.getBody().asString();
        logApiResponse("===Retrieving a deleted Customer===",getCustomer);
        ResponseValidator.validateResponseBodyContainsData(respBody,errorMsg);
    }

    @And("Verify the Customer Profile is deleted from Customer List")
    public void verifyTheCustomerProfileIsDeleted(DataTable dataTable) {

        logStep("Verifying customer details deleted");


        getAllCustomers = APIBuilder.get(
                EndPoints.Customer.GET_ALL_CUSTOMERS
                ,HeaderConfig.getDefaultHeaders()

        );

        ResponseValidator.validateResponseCode(getAllCustomers.getStatusCode(), 200);

        ResponseValidator.validateJsonPresence(getAllCustomers.jsonPath().getList(""),customerDetails,false);
        logApiResponse("Get All Customer ", getAllCustomers);
        logSuccess("Customer Deletion status validated");
    }

    @And("Verify the Customer associated accounts are deleted")
    public void verifyTheCustomerAssociatedAccountsAreDeleted(DataTable dataTable) {

        logStep("Verifying customer associated accounts deleted");
        for(Map.Entry<String,AccountInformation> account:runTimeAccts.entrySet()){

            getAccount = APIBuilder.get(EndPoints.AccountTransactions
                            .getAccountEndpoint(account.getValue().getAccountNumber().toString())
                    , HeaderConfig.getDefaultHeaders());

            ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 404);

        }

        logSuccess("Customer's associated accounts are deleted successfuly");
    }





    @Then("Verify Expected API Failure with Status Code {int}")
    public void verifyExpectedAPIFailureWithStatusCode(int expStatusCode) {

        logStep("Initiating expected API failure validation");

        int actualStatuCode = currentResponse.statusCode();
        logStep("Expected HTTP status code: " + expStatusCode);
        logStep("Actual HTTP status code: " + actualStatuCode);

        logApiResponse("NEGATIVE SCENARIO", currentResponse);

        ResponseValidator.validateResponseCode(actualStatuCode, expStatusCode);

        logStep("Successfully validated expected HTTP status code: " + expStatusCode);

        logStep("Expected API failure - Status Code validation completed successfully");

    }

    @Then("Verify Expected API Failure with Error Message {string}")
    public void verifyExpectedAPIFailureWithStatusCode(String expErrorMsg) {

        logStep("Initiating expected API failure validation");

        String actualResponseBody = currentResponse.getBody().asString();

        logStep("Expected error message: " + expErrorMsg);

        ResponseValidator.validateResponseBodyContainsData(actualResponseBody, expErrorMsg);

        logStep("Successfully validated expected error message: " + expErrorMsg);

        logStep("Expected API failure - Error message validation completed successfully");

    }

    @Then("Verify Expected API Failure with Duplicate Customer Error Message")
    public void verifyExpectedAPIFailureWithDuplicateCustomer() {

        logStep("Initiating expected API failure validation");
        String expectedErrorMsg = "Customer with id: "+customerDetails.getCustomerNumber()+" already exist";
        String actualResponseBody = currentResponse.getBody().asString();

        logStep("Expected error message: " + expectedErrorMsg);

        ResponseValidator.validateResponseBodyContainsData(actualResponseBody, expectedErrorMsg);

        logStep("Successfully validated expected error message: " + expectedErrorMsg);

        logStep("Expected API failure - Error message validation completed successfully");

    }

    @Then("Verify Expected API Failure with Duplicate Account Error Message")
    public void verifyExpectedAPIFailureWithDuplicateAccount() {

        logStep("Initiating expected API failure validation");
        String expectedErrorMsg = "Account with id: "+accountInformation.getAccountNumber()+" already exist";
        String actualResponseBody = currentResponse.getBody().asString();

        logStep("Expected error message: " + expectedErrorMsg);

        ResponseValidator.validateResponseBodyContainsData(actualResponseBody, expectedErrorMsg);

        logStep("Successfully validated expected error message: " + expectedErrorMsg);

        logStep("Expected API failure - Error message validation completed successfully");

    }


    @And("Create new Account {string} with {bigdecimal}")
    public void createNewAccount(String arg0,BigDecimal initialBalance) {
        logStep("Creating a "+arg0+" Account for the Customer");

        accountInformation = AccountInformation.getFakeActiveAccountInformation();
        runTimeAccts.put(arg0,accountInformation);

        accountInformation.setAccountBalance(initialBalance);
        addAccount = APIBuilder.post(EndPoints.AccountTransactions
                        .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                HeaderConfig.getDefaultHeaders(),
                accountInformation);


        ResponseValidator.validateResponseCode(addAccount.getStatusCode(), 201);


    }



    @When("Transfer {bigdecimal} from Account {string} to Account {string}")
    public void transferFromAccountToAccount(BigDecimal transferAmount, String arg1, String arg2) {

        AccountInformation sourceAccount = runTimeAccts.get(arg1);
        AccountInformation destinationAccount = runTimeAccts.get(arg2);
          boolean postiveAmount = (transferAmount.compareTo(new BigDecimal("0.00"))>0) && transferAmount.compareTo(sourceAccount.getAccountBalance())<0;

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

        currentResponse = transferFund;

        logApiResponse("FUND TRANSFER - PUT", transferFund);

        if(postiveAmount){
            ResponseValidator.validateResponseCode(transferFund.statusCode(),200);
            runTimeTranHistory.put(sourceAccount,runTimeTranHistory.get(sourceAccount)+1);
            runTimeTranHistory.put(destinationAccount,runTimeTranHistory.get(destinationAccount)+1);
        }else{
            ResponseValidator.validateResponseCode(transferFund.statusCode(), 400);
        }

        logStep("Fund transfer API execution completed with status: "
                + transferFund.getStatusCode());
    }



    @Then("Verify the {string} changes of transferred amount {bigdecimal} in Account {string}")
    public void verifyTheTransactionOfTransferAmountInAccount(String transaction, BigDecimal transferAmount, String account) {

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

    @Then("Retrieve the account {string} balance")
    public void retrieveTheAccountBalance(String account) {
        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(account).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );
        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);

    }

    @Then("Retrieve the account {string} balance {bigdecimal} before transfer")
    public void retrieveTheAccountBalanceBeforeTransfer(String account,BigDecimal balance) {
        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(account).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );
        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);
        ResponseValidator.validateJsonPathBigDecimal(getAccount, "accountBalance", runTimeAccts.get(account).getAccountBalance());
        logSuccess("Account balance for account: "+runTimeAccts.get(account).getAccountNumber()
                                        +" verified before transfer: " + runTimeAccts.get(account).getAccountBalance());
    }



    @And("Verify the decreased balance in the account {string} after transfer {bigdecimal}")
    public void verifyTheDecreasedBalanceInTheAccountAfterTransfer(String sourceAccount, BigDecimal transferredAmount) {
        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(sourceAccount).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );
        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);

        ResponseValidator.validateJsonPathBigDecimal(getAccount, "accountBalance", runTimeAccts.get(sourceAccount).getAccountBalance().subtract(transferredAmount));
        logSuccess("Account balance decreased in the Source account and Verified: " +  runTimeAccts.get(sourceAccount).getAccountBalance().subtract(transferredAmount));
    }

    @And("Verify the increased balance in the account {string} after transfer {bigdecimal}")
    public void verifyTheIncreasedBalanceInTheAccountAfterTransfer(String destAccount, BigDecimal transferredAmount) {
        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(destAccount).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);
        ResponseValidator.validateJsonPathBigDecimal(getAccount, "accountBalance",  runTimeAccts.get(destAccount).getAccountBalance().add(transferredAmount));
        logSuccess("Account balance increased in the Destination account and Verified: " + runTimeAccts.get(destAccount).getAccountBalance().add(transferredAmount));
    }


    @And("Retrieve the total no.of Transactions from Transaction History for the account {string} before transfer")
    public void retrieveTheTotalNoOfTransactionsFromTransactionHistoryForTheAccountBeforeTransfer(String account) {

        long accountNo = runTimeAccts.get(account).getAccountNumber();

        logStep("Retrieving Transaction History");
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

        int transactionsCount = JsonParser.parseString(transactionDetailsForAccount1.getBody().asString()).getAsJsonArray().size();
        runTimeTranHistory.put(runTimeAccts.get(account),transactionsCount);

        logStep("Retrieved no.of transactions "+transactionsCount
                    + " from Transaction history for the account "
                    + runTimeAccts.get(account).getAccountNumber() +" before Transfer");


    }

    @Then("Verify a {string} Transaction entry in Account {string} transaction history")
    public void verifyATransactionEntryInAccount(String transaction, String account) {
        long accountNo = runTimeAccts.get(account).getAccountNumber();

        logStep("Initiating transaction verification");
        logStep("Transaction type: " + transaction);
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

        int totalTransactionsCount = JsonParser.parseString(transactionDetailsForAccount1.getBody().asString()).getAsJsonArray().size();
        Assert.assertEquals(runTimeTranHistory.get(runTimeAccts.get(account)),totalTransactionsCount);

        logStep("Validating Expected total no.of Transactions "
                + runTimeTranHistory.get(runTimeAccts.get(account))
                + " is matches with actual no.of Transactions " + totalTransactionsCount);

    }
    @Then("Verify No new {string} Transaction entry in Account {string} transaction history")
    public void verifyNoNewTransactionEntryInAccount(String transaction, String account) {
        long accountNo = runTimeAccts.get(account).getAccountNumber();

        logStep("Initiating transaction verification");
        logStep("Transaction type: " + transaction);
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

        int totalTransactionsCount = JsonParser.parseString(transactionDetailsForAccount1.getBody().asString()).getAsJsonArray().size();
        Assert.assertEquals(runTimeTranHistory.get(runTimeAccts.get(account)),totalTransactionsCount);

        logStep("Validating no new "+transaction+" transaction entry observed");
        logStep("Total no. of Transaction Before: "+runTimeTranHistory.get(runTimeAccts.get(account)
                + "and After: "+totalTransactionsCount
                + " observed as unchanged for Account: "+accountNo));

    }

    @And("Verify the balance remains unchanged in the account {string} for an invalid transfer")
    public void verifyTheBalanceRemainsUnchangedInTheAccountForAnInvalidTransfer(String account) {
        logStep("Retrieving account and verifying balance");
        getAccount = APIBuilder.get(
                EndPoints.AccountTransactions.getAccountEndpoint(
                        String.valueOf(runTimeAccts.get(account).getAccountNumber())
                ),
                HeaderConfig.getDefaultHeaders()
        );

        logApiResponse("RETRIEVE ACCOUNT - GET", getAccount);
        ResponseValidator.validateResponseCode(getAccount.getStatusCode(), 200);
        ResponseValidator.validateJsonPathBigDecimal(getAccount, "accountBalance",  runTimeAccts.get(account).getAccountBalance());
        logSuccess("Account balance remains same in the account "+account);
        logSuccess("Account balance - Before: "+runTimeAccts.get(account).getAccountBalance()
                                    +"- After: "+getAccount.getBody().jsonPath().get("accountBalance")
                                    +" remains same");
    }


//    @After
//    public static void after_All(){
//
//    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario != null && scenario.isFailed()) {
                scenario.log("SCENARIO FAILED");
            }
        } finally {
            ScenarioContext.clear();
            accountNo1 = 0;
            runTimeAccts = new HashedMap<>();
            runTimeTranHistory =  null;
            currentResponse = null;

        }
    }

}
