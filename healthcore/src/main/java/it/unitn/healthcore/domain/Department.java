package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
/**
 * @class Department
 * @brief Represents a department in our system. 
 * It uses JPA annotations for ORM mapping to the "departments" table in the database.
 ** @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name = "departments")
public class Department {
    /** @brief Default constructor required by JPA. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    /** @brief Hospital associated with the department. */
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    @JsonIgnore
    private Hospital hospital;
    /** @brief List of equipments associated with the department. */
    @OneToMany (mappedBy = "department")
    private List<Equipment> equipments;
    private String name;
    private Integer beds;
    private Integer totalStaffPositions;
    /** @brief Default constructor required by JPA. */

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Doctor> doctors;

    public Department(){}
    /** @brief Creates a Department with full credentials. */
    public Department(Hospital hospital, String name, Integer beds, Integer totalStaffPositions) {
        this.hospital = hospital;
        this.name = name;
        this.beds = beds;
        this.totalStaffPositions = totalStaffPositions;
    }
    /** @brief Creates a Department . */
    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }
    /** @brief Sets the department ID. */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    /** @brief Returns the list of equipments associated with the department. */

    public String getName() {
        return name;
    }
    /** @brief Sets the name of the department. */
    public void setName(String name) {
        this.name = name;
    }
    /** @brief Returns the available bed number  within the department.*/
    public Integer getBeds() {
        return beds;
    }
    /** @brief Sets the available bed number  within the department.*/
    public void setBeds(Integer beds) {
        this.beds = beds;
    }
    /** @brief Returns the total staff positions within the department.*/
    public Integer getTotalStaffPositions() {
        return totalStaffPositions;
    }
    /** @brief Sets the total staff positions within the department.*/
    public void setTotalStaffPositions(Integer totalStaffPositions) {
        this.totalStaffPositions = totalStaffPositions;
    }
    /** @brief Returns the hospital associated with the department. */
    public Hospital getHospital() {
        return hospital;
    }
    /** @brief Sets the hospital associated with the department. */
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
