package it.unitn.healthcore.domain;
/**
 * @class PasswordConfirmationForm
 * @brief This class serves as a base form for user registration processes that require password confirmation.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */

public class PasswordConfirmationForm {
    private String email;
    private String password;
    private String passwordConfirmation;
    /** @brief Default constructor required by JPA. */
    public PasswordConfirmationForm(){}

    /** @brief Creates a PasswordConfirmationForm with the specified email, password, and password confirmation. */
    public PasswordConfirmationForm(String email, String password, String passwordConfirmation) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    /** @brief Email getter. */
    public String getEmail() {
        return email;
    }

    /** @brief Email setter. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @brief Password getter. */
    public String getPassword() {
        return password;
    }

    /** @brief Password setter. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @brief Password confirmation getter. */
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    /** @brief Password confirmation setter. */
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
