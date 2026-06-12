package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
/**
 * @class Patient
 * @brief Represents a patient in our system, extending the User class.
 * It uses JPA annotations for ORM mapping to the "patients" table in the database.
 * @details The Patient class includes attributes specific to patients, such as healthcare card number, insurance plan, and triage status. It also maintains a one-to-many relationship with appointments, allowing us to track all appointments associated with a patient.
 * @details The triage status can be used to manage patient flow in emergency situations, indicating whether a patient is currently in triage or not.
 * @details The insurance plan association allows us to determine which insurance plan a patient has and whether it covers specific hospitals or treatments.
 * @details The healthcare card number is a unique identifier for patients, which can be used for administrative and billing purposes
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table (name="patients")
@DiscriminatorValue("Patient")
public class Patient extends User{

    private Integer healthcareCardNumber;
    @ManyToOne
    @JoinColumn(name = "insurance_plan_id")
    private InsurancePlan insurancePlan;
    private String triageStatus;

    @OneToMany (mappedBy = "patient")
    @JsonIgnore
    private List<Appointment> appointments;
    /** @brief Default constructor required by JPA. */
    public Patient(){

    }
    /**
     * @brief Creates a Patient with full credentials.
     * @param name     First name.
     * @param surname  Last name.
     * @param email    Email address.
     * @param password Account password.
     * @param healthcare_card_number Unique healthcare card number for the patient.
     * @return A new instance of Patient with the specified attributes, with insurance plan set to null and triage status set to "NotInTriage".
     */
    public  Patient(String name, String surname, String email, String password, Integer healthcare_card_number){
        super(name, surname, email, password);
        this.healthcareCardNumber = healthcare_card_number;
        this.insurancePlan = null;
        this.triageStatus = "NotInTriage";
    }

    /** @brief It creates full insurance plan including Patient */   
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    /** @brief Getter for Appointment */ 
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /** @brief Setter for Appointment */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /** @brief Healthcare card number getter. */
    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }
    /** @brief Healthcare card number setter. */
    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }
    /** @brief Insurance plan getter. */
    public InsurancePlan getInsurancePlan() {
        return insurancePlan;
    }
    /** @brief Insurance plan setter. */
    public void setInsurancePlan(InsurancePlan insurancePlan) {
        this.insurancePlan = insurancePlan;
    }
    /** @brief Triage status getter. */
    public String getTriageStatus() {
        return triageStatus;
    }
    /** @brief Triage status setter. */
    public void setTriageStatus(String triageStatus) {
        this.triageStatus = triageStatus;
    }
}

