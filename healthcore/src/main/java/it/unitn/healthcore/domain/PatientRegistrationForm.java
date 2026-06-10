package it.unitn.healthcore.domain;

public class PatientRegistrationForm {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String passwordConfirmation;
    private Integer healthcareCardNumber;

    public PatientRegistrationForm(){

    }

    public PatientRegistrationForm(Integer id, String name, String surname, String email, String password, String passwordConfirmation, Integer healthcareCardNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }

    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }
}
