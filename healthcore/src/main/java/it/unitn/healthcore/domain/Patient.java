package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

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

    public Patient(){

    }

    public  Patient(String name, String surname, String email, String password, Integer healthcare_card_number){
        super(name, surname, email, password);
        this.healthcareCardNumber = healthcare_card_number;
        this.insurancePlan = null;
        this.triageStatus = "NotInTriage";
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }

    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }

    public InsurancePlan getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(InsurancePlan insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public String getTriageStatus() {
        return triageStatus;
    }

    public void setTriageStatus(String triageStatus) {
        this.triageStatus = triageStatus;
    }
}

