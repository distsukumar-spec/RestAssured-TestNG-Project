package utils;

public class EndPoints {

    public static class AccountTransactions {
        public static String TRANSFER_FUND = "/accounts/transfer/{customerNumber}";
        public static String ADD_ACCOUNT = "/accounts/add/{customerNumber}";
        public static String GET_ACCOUNT = "/accounts/{accountNumber}";
        public static String GET_ALL_TRANSACTIONS = "/accounts/transactions/{accountNumber}";

        public static String getAllTransactionsEndpoint(String accountNumber) {
            return GET_ALL_TRANSACTIONS.replace("{accountNumber}", accountNumber);
        }

        public static String getAccountEndpoint(String accountNumber) {
            return GET_ACCOUNT.replace("{accountNumber}", accountNumber);
        }

        public static String getAddAccountEndpoint(String customerNumber) {
            return ADD_ACCOUNT.replace("{customerNumber}", customerNumber);
        }
        public static String getTransferFundEndpoint(String customerNumber) {
            return TRANSFER_FUND.replace("{customerNumber}", customerNumber);
        }
    }

    public static class Customer {

        public static String GET_CUSTOMER = "/customers/{customerNumber}";
        public  static String UPDATE_CUSTOMER = "/customers/{customerNumber}";
        public static String DELETE_CUSTOMER = "/customers/{customerNumber}";
        public static final String ADD_CUSTOMER = "/customers/add";
        public static final String GET_ALL_CUSTOMERS = "/customers/all";

        public static String getCustomerEndpoint(String customerNumber) {
            return GET_CUSTOMER.replace("{customerNumber}", customerNumber);
        }

        public static String updateCustomerEndpoint(String customerNumber) {
            return UPDATE_CUSTOMER.replace("{customerNumber}", customerNumber);
        }

        public static String deleteCustomerEndpoint(String customerNumber) {
            return DELETE_CUSTOMER.replace("{customerNumber}", customerNumber);
        }
    }

}
