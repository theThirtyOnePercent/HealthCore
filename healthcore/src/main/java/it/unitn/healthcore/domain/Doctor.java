package it.unitn.healthcore.domain;

import jakarta.persistence.*;

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
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private String specialization;
    private Double appointmentPrice;
    @OneToMany(mappedBy = "doctor")
    private List<EquipmentDoctor> equipments;
    @OneToMany (mappedBy = "doctor", fetch = FetchType.EAGER)
    private List<Shift> shifts;
    @OneToMany (mappedBy = "doctor")
    private List<Appointment> appointments;

    public Doctor(){

    }

    public Doctor(String name, String surname, String email, String password, Department department, String specialization, Double appointmentPrice) {
        super(name, surname, email, password);
        this.department = department;
        this.specialization = specialization;
        this.appointmentPrice = appointmentPrice;
    }

    public Doctor(String name, String surname, String email, String password, Department department) {
        super(name, surname, email, password);
        this.department = department;
        this.specialization = null;
        this.appointmentPrice = null;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<EquipmentDoctor> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentDoctor> equipments) {
        this.equipments = equipments;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

