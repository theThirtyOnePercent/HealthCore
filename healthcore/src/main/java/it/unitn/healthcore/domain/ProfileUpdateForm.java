package it.unitn.healthcore.domain;

public class ProfileUpdateForm extends PasswordConfirmationForm {

    private String name;
    private String surname;
    private String specialization; //Works for doctor only

    public ProfileUpdateForm() {}

    public ProfileUpdateForm(String email, String password, String passwordConfirmation, String name, String surname, String specialization) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
    }

    public ProfileUpdateForm(String email, String password, String passwordConfirmation, String name, String surname) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.specialization = null;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
