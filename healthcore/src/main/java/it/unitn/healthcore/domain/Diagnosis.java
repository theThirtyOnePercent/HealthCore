package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jshell.Diag;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diagnosisId;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    private LocalDateTime dateRecord;
    private LocalDateTime dateStartCondition;
    private LocalDateTime dateEndCondition;

    @Lob
    private String description;

    public Diagnosis(){}

    public Diagnosis(Appointment appointment, LocalDateTime dateStartCondition, LocalDateTime dateEndCondition, String description) {
        this.appointment = appointment;
        this.dateRecord = LocalDateTime.now();
        this.dateStartCondition = dateStartCondition;
        this.dateEndCondition = dateEndCondition;
        this.description = description;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public LocalDateTime getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(LocalDateTime dateRecord) {
        this.dateRecord = dateRecord;
    }

    public LocalDateTime getDateStartCondition() {
        return dateStartCondition;
    }

    public void setDateStartCondition(LocalDateTime dateStartCondition) {
        this.dateStartCondition = dateStartCondition;
    }

    public LocalDateTime getDateEndCondition() {
        return dateEndCondition;
    }

    public void setDateEndCondition(LocalDateTime dateEndCondition) {
        this.dateEndCondition = dateEndCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
