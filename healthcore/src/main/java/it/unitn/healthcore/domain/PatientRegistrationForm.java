package it.unitn.healthcore.domain;

public class PatientRegistrationForm extends PasswordConfirmationForm {
    private String name;
    private String surname;
    private Integer healthcareCardNumber;

    public PatientRegistrationForm(){}

    public PatientRegistrationForm(String name, String surname, String email, String password, String passwordConfirmation, Integer healthcareCardNumber) {

        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.healthcareCardNumber = healthcareCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }

    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }
}
