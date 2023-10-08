package groupB.newbankV5.customercare.controllers.dto;

import groupB.newbankV5.customercare.entities.CustomerProfile;

public class CustomerProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String BirthDate;
    private String FiscalCountry;
    private String address;

    public CustomerProfileDto() {
    }

    public CustomerProfileDto(Long id, String firstName, String lastName, String email, String phoneNumber, String birthDate, String fiscalCountry, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        BirthDate = birthDate;
        FiscalCountry = fiscalCountry;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getFiscalCountry() {
        return FiscalCountry;
    }

    public void setFiscalCountry(String fiscalCountry) {
        FiscalCountry = fiscalCountry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static CustomerProfileDto customerProfileFactory(CustomerProfile customerProfile) {
        return new CustomerProfileDto(
                customerProfile.getId(),
                customerProfile.getFirstName(),
                customerProfile.getLastName(),
                customerProfile.getEmail(),
                customerProfile.getPhoneNumber(),
                customerProfile.getBirthDate(),
                customerProfile.getFiscalCountry(),
                customerProfile.getAddress()
        );
    }
}
