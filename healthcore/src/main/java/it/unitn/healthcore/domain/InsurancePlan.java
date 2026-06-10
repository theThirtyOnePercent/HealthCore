package it.unitn.healthcore.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "InsurancePlans")
public class InsurancePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer insurancePlanId;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "Insurances_Hospitals",
            joinColumns = @JoinColumn(name = "insurance_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "hospital_id")
    )
    private List<Hospital> hospitals;

    public InsurancePlan(){}

    public InsurancePlan(String name) {
        this.name = name;
    }

    public Integer getInsurancePlanId() {
        return insurancePlanId;
    }

    public void setInsurancePlanId(Integer insurancePlanId) {
        this.insurancePlanId = insurancePlanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
