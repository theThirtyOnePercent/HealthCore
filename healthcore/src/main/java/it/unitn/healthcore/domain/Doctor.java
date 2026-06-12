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
    /** @brief Default constructor required by JPA. */
    public Doctor(){

    }
    /**
     * @brief Creates a Doctor with full credentials.
     * @param name     First name.
     * @param surname  Last name.
     * @param email    Email address.
     * @param password Account password.*/
    public Doctor(String name, String surname, String email, String password, Department department, String specialization, Double appointmentPrice) {
        super(name, surname, email, password);
        this.department = department;
        this.specialization = specialization;
        this.appointmentPrice = appointmentPrice;
    }
    /**
     * @brief Creates a Doctor with full credentials.*/
    public Doctor(String name, String surname, String email, String password, Department department) {
        super(name, surname, email, password);
        this.department = department;
        this.specialization = null;
        this.appointmentPrice = null;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }


    public List<Appointment> getAppointments() {
        return appointments;
    }

     /**
     * @brief Appointment getter.*/
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     * @brief Equipment getter.*/
    public List<EquipmentDoctor> getEquipments() {
        return equipments;
    }

     /**
     * @brief Appointment getter.*/
    public void setEquipments(List<EquipmentDoctor> equipments) {
        this.equipments = equipments;
    }
    /**
     * @brief Shift getter.*/
    public List<Shift> getShifts() {
        return shifts;
    }
    /**
     * @brief Shift setter.*/
    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
    /** @brief Department getter. */
    public Department getDepartment() {
        return department;
    }
    /** @brief Department setter. */
    public void setDepartment(Department department) {
        this.department = department;
    }
    /** @brief Specialization getter. */
    public String getSpecialization() {
        return specialization;
    }
    /** @brief Specialization setter. */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    /** @brief Appointment price getter.*/
    public Double getAppointmentPrice() {
        return appointmentPrice;
    }
    /** @brief Appointment price setter.*/
    public void setAppointmentPrice(Double appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
    }
}

