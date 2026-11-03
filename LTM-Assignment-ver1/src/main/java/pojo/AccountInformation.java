package pojo;

import com.google.gson.JsonObject;
import net.datafaker.Faker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountInformation {

    private Long accountNumber;
    private BankInformation bankInformation;
    private String accountStatus;
    private String accountType;
    private Double accountBalance;
    private String accountCreated;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankInformation getBankInformation() {
        return bankInformation;
    }

    public void setBankInformation(BankInformation bankInformation) {
        this.bankInformation = bankInformation;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountCreated() {
        return accountCreated;
    }



    public static AccountInformation getFakeAccountInformation(){
        Faker faker = new Faker();
        AccountInformation accountInfo = new AccountInformation();
        // Set fake values for each field
        accountInfo.setAccountNumber(faker.number().randomNumber(10));
        accountInfo.setAccountStatus(faker.options().option("Active", "Inactive"));
        accountInfo.setAccountType(faker.options().option("Checking", "Savings"));
        accountInfo.setAccountBalance(faker.number().randomDouble(2, 1000, 10000));
        accountInfo.setAccountCreated();
        accountInfo.setBankInformation(BankInformation.getFakeBankInformation());
        return accountInfo;
    }

    private void setAccountCreated() {
        this.accountCreated = DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(java.time.ZoneOffset.UTC)
                .format(Instant.now());
//        System.out.println("Account Created: " + this.accountCreated);
    }

    @Override
    public String toString() {
        return "{accountNumber=" + accountNumber + ", bankInformation=" + bankInformation + ", accountStatus=" + accountStatus +
                ", accountType=" + accountType + ", accountBalance=" + accountBalance + ", accountCreated=" + accountCreated + "}";
    }
}

