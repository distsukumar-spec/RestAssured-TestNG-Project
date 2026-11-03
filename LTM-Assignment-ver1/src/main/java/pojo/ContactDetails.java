package pojo;

import net.datafaker.Faker;

public class ContactDetails {
    private String emailId;
    private String homePhone;
    private String workPhone;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public static ContactDetails getFakeContactDetails() {
        Faker faker = new Faker();
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setEmailId(faker.internet().emailAddress());
        contactDetails.setHomePhone(faker.phoneNumber().cellPhone());
        contactDetails.setWorkPhone(faker.phoneNumber().cellPhone());
        return contactDetails;
    }

    public String toString() {
        return "{emailId=" + emailId + ", homePhone=" + homePhone + ", workPhone=" + workPhone + "}";
    }
}

