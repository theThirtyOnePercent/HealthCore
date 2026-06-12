package it.unitn.healthcore.domain;
/**
 * @class ProfileUpdateForm
 * @brief This class represents the form used for updating user profiles, extending the PasswordConfirmationForm to include additional fields such as name, surname, and specialization (for doctors).
 * @see PasswordConfirmationForm
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public class ProfileUpdateForm extends PasswordConfirmationForm {

    private String name;
    private String surname;
    private String specialization; //Works for doctor only
    /** @brief Default constructor required by JPA. */
    public ProfileUpdateForm() {}

    /** @brief Creates a ProfileUpdateForm with the specified details. */
    public ProfileUpdateForm(String email, String password, String passwordConfirmation, String name, String surname, String specialization) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
    }

    /** @brief Creates a ProfileUpdateForm with the specified details, without specialization. */
    public ProfileUpdateForm(String email, String password, String passwordConfirmation, String name, String surname) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.specialization = null;
    }

    /** @brief Name getter. */
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

    /** @brief Specialization getter. */
    public String getSpecialization() {
        return specialization;
    }

    /** @brief Specialization setter. */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
