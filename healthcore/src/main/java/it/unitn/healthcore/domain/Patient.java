package it.unitn.healthcore.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table (name="patients")
@DiscriminatorValue("Patient")
public class Patient extends User {

    private Integer healthcare_card_number;
    private Integer insurance_plan_id;
    private String triage_status;


}
