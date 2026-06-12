package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
/**
 * @class User
 * @brief Represents a general user in our system.
 * This class serves as a base for specific user roles like Patient, Doctor, and Administrator.
 * It uses JPA annotations for ORM mapping to the "users" table in the database.
 ** @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn (name="role")
public class User {
    /**
     * Common attributes for all users.
     * - id: Unique identifier for the user (primary key).
     * - name: First name of the user.
     * - surname: Last name of the user.
     * - email: Email address of the user (used for login).
     * - password: Password for authentication. 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String email;
    @JsonIgnore
    private String password;
    /** @brief Default constructor required by JPA. */
    public User(){

    }
    /**
     * @brief Creates a User with full credentials.
     * @param name     First name.
     * @param surname  Last name.
     * @param email    Email address.
     * @param password Account password.
     */
    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
    /** @brief Returns a string representation of the User. */
    public Integer getId() {
        return id;
    }
    /** @brief Sets the user ID. */
    public void setId(Integer id) {
        this.id = id;
    }
    /** @brief Returns the first name of the user. */
    public String getName() {
        return name;
    }
    /** @brief Sets the first name of the user. */
    public void setName(String name) {
        this.name = name;
    }
    /** @brief Returns the last name of the user. */
    public String getSurname() {
        return surname;
    }
    /** @brief Sets the last name of the user. */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /** @brief Returns the email address of the user. */
    public String getEmail() {
        return email;
    }
    /** @brief Sets the email address of the user. */
    public void setEmail(String email) {
        this.email = email;
    }
    /** @brief Returns the password of the user. */
    public String getPassword() {
        return password;
    }
    /** @brief Sets the password of the user. */
    public void setPassword(String password) {
        this.password = password;
    }

}
