package it.unitn.healthcore.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    private Boolean isValidPassword(){
        if (this.password == null) {
            return false;
        }

        // at least 8 chars, one uppercase, one lowercase, one digit, one special char
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

        return this.password.matches(passwordRegex);
    }

    private boolean isValidEmail() {
        if (this.email == null) {
            return false;
        }

        //checks for allowed characters (letters, numbers, and ._%+-),
        // followed by a single @ symbol,
        // then a valid domain name containing letters, numbers, dots, or hyphens,
        // and finally a domain extension with at least two letters (e.g., .com, .it)
        String emailRegex =
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return this.email.matches(emailRegex);
    }

    public void isValidPasswordForm (){

        //Checks if it is not null
        if (this.getPassword() == null || this.getPasswordConfirmation() == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password was not given");
        }

        if (!isValidEmail()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"email is not valid");
        }

        //Checks if they are the same
        if (!this.getPassword().equals(this.getPasswordConfirmation())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not match with password confirmation");
        }
        //Checks if it complies with the guidelines
        if (!isValidPassword()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not meet security requirements");
        }
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
