
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.AccountInformation;
import pojo.ContactDetails;
import pojo.CustomerDetails;
import pojo.TransferDetails;
import utils.*;


public class TestSuite {

   // @Test
    public void testLifeCycle() {

        CustomerDetails customerDetails = CustomerDetails.getFakeCustomerDetails();

        Response addCustomer = APIBuilder.post(EndPoints.Customer.ADD_CUSTOMER,
                                                HeaderConfig.getDefaultHeaders(),
                                                customerDetails);

        ResponseValidator.validateResponseCode(addCustomer.getStatusCode(), 201);

        Response getCustomer = APIBuilder.get(EndPoints.Customer.getCustomerEndpoint(customerDetails.getCustomerNumber().toString()),
                                            HeaderConfig.getDefaultHeaders());

        ResponseValidator.validateResponseCode(getCustomer.getStatusCode(), 200);

        ContactDetails existingContactDetails = getCustomer.jsonPath().getObject("contactDetails", ContactDetails.class);
        System.out.println("Existing Contact Details: " + existingContactDetails +" for Customer Number: " + customerDetails.getCustomerNumber());

        ContactDetails newContactDetails = ContactDetails.getFakeContactDetails();
        System.out.println("Contact Details to be updated: " + newContactDetails +" for Customer Number: " + customerDetails.getCustomerNumber());

        customerDetails.setContactDetails(newContactDetails);
        Response updateCustomer = APIBuilder.put(EndPoints.Customer.updateCustomerEndpoint(customerDetails.getCustomerNumber().toString()),
                                                HeaderConfig.getDefaultHeaders(),
                                                customerDetails);

        ResponseValidator.validateResponseCode(updateCustomer.getStatusCode(), 200);
        Response getUpdatedCustomer = APIBuilder.get(EndPoints.Customer.getCustomerEndpoint(customerDetails.getCustomerNumber().toString()),
                                            HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(getUpdatedCustomer.getStatusCode(), 200);
        ResponseValidator.validateJsonPathData(getUpdatedCustomer, "contactDetails.emailId", newContactDetails.getEmailId());
        ResponseValidator.validateJsonPathData(getUpdatedCustomer, "contactDetails.homePhone", newContactDetails.getHomePhone());
        ResponseValidator.validateJsonPathData(getUpdatedCustomer, "contactDetails.workPhone", newContactDetails.getWorkPhone());

        Response deleteCustomer = APIBuilder.delete(EndPoints.Customer.deleteCustomerEndpoint(customerDetails.getCustomerNumber().toString()),
                                                    HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(deleteCustomer.getStatusCode(), 200);

        Response getDeletedCustomer = APIBuilder.get(EndPoints.Customer.getCustomerEndpoint(customerDetails.getCustomerNumber().toString()),
                                            HeaderConfig.getDefaultHeaders());
//        ResponseValidator.validateResponseCode(getDeletedCustomer.getStatusCode(), 404);

        Response getAllCustomers = APIBuilder.get(EndPoints.Customer.GET_ALL_CUSTOMERS,
                                            HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(getAllCustomers.getStatusCode(), 200);

        getAllCustomers.jsonPath().getList("",CustomerDetails.class).contains(customerDetails);
        ResponseValidator.validateJsonPresence(getAllCustomers.jsonPath().getList(""),customerDetails,false);

    }


    @Test
    public void testSuccessTransfer(){

        CustomerDetails customerDetails = CustomerDetails.getFakeCustomerDetails();

        Response addCustomer = APIBuilder.post(EndPoints.Customer.ADD_CUSTOMER,
                HeaderConfig.getDefaultHeaders(),
                customerDetails);

        ResponseValidator.validateResponseCode(addCustomer.getStatusCode(), 201);

        AccountInformation account1 = AccountInformation.getFakeAccountInformation();
        AccountInformation account2 = AccountInformation.getFakeAccountInformation();

        Response addAccount1 = APIBuilder.post(EndPoints.AccountTransactions
                                                .getAddAccountEndpoint(customerDetails.getCustomerNumber().toString()),
                                                HeaderConfig.getDefaultHeaders(),
                                                account1);

        System.out.println("Customer Details: " + customerDetails.getCustomerNumber());
        System.out.println("Account 1 Details: " + account1);

        ResponseValidator.validateResponseCode(addAccount1.getStatusCode(), 201);

        Response getAccount1 = APIBuilder.get(EndPoints.AccountTransactions.getAccountEndpoint(account1.getAccountNumber().toString())
                                            ,HeaderConfig.getDefaultHeaders());
        System.out.println("Account 1 Details: " + account1.getAccountNumber());
        System.out.println(account1);

        ResponseValidator.validateResponseCode(getAccount1, 200,true);

        Response addAccount2 = APIBuilder.post(EndPoints.AccountTransactions.getAddAccountEndpoint(customerDetails.getCustomerNumber().toString())
                                               ,HeaderConfig.getDefaultHeaders(),
                                                account2);

        ResponseValidator.validateResponseCode(addAccount2.getStatusCode(), 201);

        Response getAccount2 = APIBuilder.get(EndPoints.AccountTransactions.getAccountEndpoint(account2.getAccountNumber().toString())
                                              ,HeaderConfig.getDefaultHeaders());
        ResponseValidator.validateResponseCode(getAccount2, 200,true);

        TransferDetails transferDetails = TransferDetails.getTransferPayload(account1.getAccountNumber()
                                            ,account2.getAccountNumber()
                                            ,1000.99);
        System.out.println("Transfer Details: " + transferDetails);
        Response transferFund = APIBuilder.put(EndPoints.AccountTransactions.getTransferFundEndpoint(customerDetails.getCustomerNumber().toString()),
                                                HeaderConfig.getDefaultHeaders()
                                                ,transferDetails);

        ResponseValidator.validateResponseCode(transferFund.getStatusCode(), 200);

        Response transactionDetailsForAccount1 = APIBuilder.get(EndPoints.AccountTransactions
                                                            .getAllTransactionsEndpoint(account1.getAccountNumber().toString()),
                                                             HeaderConfig.getDefaultHeaders());

        ResponseValidator.validateResponseCode(transactionDetailsForAccount1.getStatusCode(), 200);

        Response transactionDetailsForAccount2 = APIBuilder.get(EndPoints.AccountTransactions
                .getAllTransactionsEndpoint(account2.getAccountNumber().toString()),
                HeaderConfig.getDefaultHeaders());

        ResponseValidator.validateResponseCode(transactionDetailsForAccount2.getStatusCode(), 200);


    }



}
