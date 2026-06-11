package it.unitn.healthcore.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
/**
 * @class Doctor
 * @brief Represents a doctor in our system, extending the general User class.
 * T
 * It uses JPA annotations for ORM mapping to the "doctors" table in the database.
 ** @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Entity
@Table(name="doctors")
@DiscriminatorValue("Doctor")
public class Doctor extends User{
    private Integer departmentId;
    private String specialization;
    private Double appointmentPrice;
    @OneToMany(mappedBy = "doctor")
    private List<EquipmentDoctor> equipments;

    public Doctor(){

    }

    public Doctor(String name, String surname, String email, String password, Integer departmentId, String specialization, Double appointmentPrice) {
        super(name, surname, email, password);
        this.departmentId = departmentId;
        this.specialization = specialization;
        this.appointmentPrice = appointmentPrice;
    }

    public Doctor(String name, String surname, String email, String password, Integer departmentId) {
        super(name, surname, email, password);
        this.departmentId = departmentId;
        this.specialization = null;
        this.appointmentPrice = null;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Double getAppointmentPrice() {
        return appointmentPrice;
    }

    public void setAppointmentPrice(Double appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
    }
}

