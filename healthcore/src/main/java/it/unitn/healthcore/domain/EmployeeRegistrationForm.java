package it.unitn.healthcore.domain;
/**
 * @class 
 * @brief 
 ** @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
public class EmployeeRegistrationForm extends PasswordConfirmationForm{
    private String name;
    private String surname;
    private String role;
    private Integer departmentId;
    /** @brief Default constructor required by JPA.*/
    public EmployeeRegistrationForm(){}
    /** @brief Default constructor required by JPA.*/
    public EmployeeRegistrationForm(String email, String password, String passwordConfirmation, String name, String surname, String role, Integer departmentId) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.departmentId = departmentId;
    }
    /** @brief Default constructor required by JPA.*/
    public EmployeeRegistrationForm(String email, String password, String passwordConfirmation, String name, String role, String surname) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.departmentId = null;
    }

    /** @brief Name getter.*/
    public String getName() {
        return name;
    }

    /** @brief Name setter.*/
    public void setName(String name) {
        this.name = name;
    }

    /** @brief Surname getter.*/
    public String getSurname() {
        return surname;
    }
    /** @brief Surname setter.*/
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /** @brief Role getter.*/
    public String getRole() {
        return role;
    }

    /** @brief Role setter.*/
    public void setRole(String role) {
        this.role = role;
    }

    /** @brief Department ID getter.*/
    public Integer getDepartmentId() {
        return departmentId;
    }

    /** @brief Department ID setter.*/
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
