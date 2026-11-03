package pojo;

import net.datafaker.Faker;

public class BankInformation {
    private String branchName;
    private Integer branchCode;
    private AddressDetails branchAddress;
    private Integer routingNumber;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public AddressDetails getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(AddressDetails branchAddress) {
        this.branchAddress = branchAddress;
    }

    public Integer getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(Integer routingNumber) {
        this.routingNumber = routingNumber;
    }

    public static BankInformation getFakeBankInformation() {
        Faker faker = new Faker();
        BankInformation bankInfo = new BankInformation();
        bankInfo.setBranchName(faker.company().name());
        bankInfo.setBranchCode(faker.number().numberBetween(1000, 9999));
        AddressDetails address = AddressDetails.getFakeAddressDetails();
        bankInfo.setBranchAddress(address);
        bankInfo.setRoutingNumber(faker.number().numberBetween(100000000, 999999999));
        return bankInfo;
    }

    @Override
    public String toString() {
        return "{branchName=" + branchName + ", branchCode=" + branchCode + ", branchAddress=" + branchAddress +
                ", routingNumber=" + routingNumber + "}";
    }

}

