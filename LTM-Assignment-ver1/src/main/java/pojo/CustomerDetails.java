package pojo;

import net.datafaker.Faker;

public class CustomerDetails {
    private String firstName;
    private String lastName;
    private String middleName;
    private Long customerNumber;
    private String status;
    private AddressDetails customerAddress;
    private ContactDetails contactDetails;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AddressDetails getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(AddressDetails customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public static CustomerDetails getFakeCustomerDetails() {
        Faker faker = new Faker();
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName(faker.name().firstName());
        customerDetails.setLastName(faker.name().lastName());
        customerDetails.setMiddleName(faker.name().nameWithMiddle());
        customerDetails.setCustomerNumber(faker.number().numberBetween(1000L, 99999L));
        customerDetails.setStatus(faker.options().option("Active", "Inactive"));
        customerDetails.setCustomerAddress(AddressDetails.getFakeAddressDetails());
        customerDetails.setContactDetails(ContactDetails.getFakeContactDetails());
        return customerDetails;
    }

    @Override
    public String toString() {
        return "CustomerDetails [firstName=" + firstName + ", lastName=" + lastName + ", middleName=" + middleName
                + ", customerNumber=" + customerNumber + ", status=" + status + ", customerAddress=" + customerAddress
                + ", contactDetails=" + contactDetails + "]";
    }
}

