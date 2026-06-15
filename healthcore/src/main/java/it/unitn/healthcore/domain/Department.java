package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
/**
 * @class Department
 * @brief Entity representing a hospital department, mapped to the "departments" table.
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
    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Doctor> doctors;
    /** @brief Default constructor required by JPA. */
    public Department(){}

    /** @brief Constructor for creating Department with hospital, name, beds, totalStaffPositions  */
    public Department(Hospital hospital, String name, Integer beds, Integer totalStaffPositions) {
        this.hospital = hospital;
        this.name = name;
        this.beds = beds;
        this.totalStaffPositions = totalStaffPositions;
    }

    /** @brief Returns list of equipments available  */
    public List<Equipment> getEquipments() {
        return equipments;
    }
     /** @brief Sets a equipment of the Department  */
    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }
    /** @brief Returns the list of doctors associated with the department. */
    public List<Doctor> getDoctors() {
        return doctors;
    }
    /** @brief Sets the list of doctors associated with the department. */
    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    /** @brief Department ID getter. */
    public Integer getDepartmentId() {
        return departmentId;
    }
    /** @brief Sets the department ID. */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    /** @brief getter for Department name. */
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
