package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
/**
 * @class InsurancePlan
 * @brief  Represents an insurance plan in our system, mapped to the "insuranceplans" table in the database.
 * It maintains a many-to-many relationship with Hospital, indicating which hospitals accept this insurance plan.
 * Helps determine if a patient's insurance covers a specific hospital.
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */

@Entity
@Table(name = "insuranceplans")
public class InsurancePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer insurancePlanId;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "InsurancesHospitals",
            joinColumns = @JoinColumn(name = "insurance_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "hospital_id")
    )
    @JsonIgnore
    /** @brief List of hospitals that accept this insurance plan. */
    private List<Hospital> hospitals;

    /** @brief Default constructor required by JPA. */
    public InsurancePlan(){}

    /**
     * @brief Creates an InsurancePlan with a given name.
     * @param name The name of the insurance plan.
     */
    public InsurancePlan(String name) {
        this.name = name;
    }

    /**
    * @brief Checks if this insurance plan covers a specific hospital.
    * @param hospital The hospital to check coverage for.
    * @return true if the insurance plan covers the hospital, false otherwise.
    */
    public boolean isCoveredAtHospital(Hospital hospital) {
        if (hospital == null || this.hospitals == null) {
            return false;
        }

        for (Hospital h : this.hospitals) {
            if (h.getHospitalId().equals(hospital.getHospitalId())) {
                return true;
            }
        }

        return false;
    }

    /** @brief Returns the insurance plan ID. */
    public Integer getInsurancePlanId() {
        return insurancePlanId;
    }

    /** @brief Sets the insurance plan ID. */
    public void setInsurancePlanId(Integer insurancePlanId) {
        this.insurancePlanId = insurancePlanId;
    }

    /** @brief Returns the insurance plan name. */
    public String getName() {
        return name;
    }

    /** @brief Sets the insurance plan name. */
    public void setName(String name) {
        this.name = name;
    }

    /** @brief Returns the list of hospitals that accept this insurance plan. */
    public List<Hospital> getHospitals() {
        return hospitals;
    }

    /** @brief Sets the list of hospitals that accept this insurance plan. */
    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
