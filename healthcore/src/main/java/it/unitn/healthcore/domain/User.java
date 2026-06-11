package it.unitn.healthcore.domain;

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
    private String password;

    public User(){

    }

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

}
