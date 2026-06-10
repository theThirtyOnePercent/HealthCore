package it.unitn.healthcore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table (name="patients")
@DiscriminatorValue("Patient")
public class Patient extends User{

    private Integer healthcareCardNumber;
    private Integer insurancePlanId;
    private String triageStatus;

    public Patient(){

    }

    public Patient(String name, String surname, String email, String password, Integer healthcare_card_number, Integer insurance_plan_id, String triage_status) {
        super(name, surname, email, password);
        this.healthcareCardNumber = healthcare_card_number;
        this.insurancePlanId = insurance_plan_id;
        this.triageStatus = triage_status;
    }

    public  Patient(String name, String surname, String email, String password, Integer healthcare_card_number){
        super(name, surname, email, password);
        this.healthcareCardNumber = healthcare_card_number;
        this.insurancePlanId = null;
        this.triageStatus = "NotInTriage";
    }

    public Integer getHealthcareCardNumber() {
        return healthcareCardNumber;
    }

    public void setHealthcareCardNumber(Integer healthcareCardNumber) {
        this.healthcareCardNumber = healthcareCardNumber;
    }

    public Integer getInsurancePlanId() {
        return insurancePlanId;
    }

    public void setInsurancePlanId(Integer insurancePlanId) {
        this.insurancePlanId = insurancePlanId;
    }

    public String getTriageStatus() {
        return triageStatus;
    }

    public void setTriageStatus(String triageStatus) {
        this.triageStatus = triageStatus;
    }
}

