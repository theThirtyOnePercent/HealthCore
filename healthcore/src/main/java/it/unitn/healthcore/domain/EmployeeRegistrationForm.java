package it.unitn.healthcore.domain;

public class EmployeeRegistrationForm extends PasswordConfirmationForm{

    private String name;
    private String surname;
    private String role;
    private Integer departmentId;

    public EmployeeRegistrationForm(){}

    public EmployeeRegistrationForm(String email, String password, String passwordConfirmation, String name, String surname, String role, Integer departmentId) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.departmentId = departmentId;
    }

    public EmployeeRegistrationForm(String email, String password, String passwordConfirmation, String name, String role, String surname) {
        super(email, password, passwordConfirmation);
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.departmentId = null;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
