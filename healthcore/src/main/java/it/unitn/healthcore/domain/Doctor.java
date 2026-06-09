package it.unitn.healthcore.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="doctors")
@DiscriminatorValue("Doctor")
public class Doctor extends User{
    private String specialization;
    private Double appointmentPrice;
}
