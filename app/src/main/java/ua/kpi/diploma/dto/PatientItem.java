package ua.kpi.diploma.dto;

/**
 * @author Mykola Yashchenko
 */
public class PatientItem {
    private String firstName;
    private String lastName;
    private String id;
    private String dateOfBirth;
    private String gender;
    private AddressItem address;
    private PassportItem passport;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public AddressItem getAddress() {
        return address;
    }

    public void setAddress(AddressItem address) {
        this.address = address;
    }

    public PassportItem getPassport() {
        return passport;
    }

    public void setPassport(PassportItem passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }
}
