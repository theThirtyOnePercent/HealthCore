package it.unitn.healthcore.domain;

public class PatientRegistrationForm extends PasswordConfirmationForm {
    private Integer id;
    private String name;
    private String surname;
    private Integer healthcareCardNumber;

    public PatientRegistrationForm(){}

    public PatientRegistrationForm(Integer id, String name, String surname, String email, String password, String passwordConfirmation, Integer healthcareCardNumber) {

        super(email, password, passwordConfirmation);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.healthcareCardNumber = healthcareCardNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
