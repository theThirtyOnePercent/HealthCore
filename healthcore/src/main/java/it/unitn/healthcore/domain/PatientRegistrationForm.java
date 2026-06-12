package it.unitn.healthcore.domain;

/**
 * @class PatientRegistrationForm
 * @brief This class represents the form used for patient registration, extending the PasswordConfirmationForm to include additional fields specific to patients.
 * @see PasswordConfirmationForm
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public class PatientRegistrationForm extends PasswordConfirmationForm {
    private String name;
    private String surname;
    private Integer healthcareCardNumber;

    /** @brief Default constructor required by JPA. */
    public PatientRegistrationForm(){}

    /** @brief Creates a PatientRegistrationForm with the specified details. */
    public PatientRegistrationForm(String name, String surname, String email, String password, String passwordConfirmation, Integer healthcareCardNumber) {

        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.healthcareCardNumber = healthcareCardNumber;
    }

    /** @brief Creates a PatientRegistrationForm with the specified details, without healthcare card number. */
    public String getName() {
        return name;
    }

    /** @brief Name setter. */
    public void setName(String name) {
        this.name = name;
    }

    /** @brief Surname getter. */
    public String getSurname() {
        return surname;
    }

    /** @brief Surname setter. */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /** @brief Healthcare card number getter. */
    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }
    /** @brief Healthcare card number setter. */
    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }
}
