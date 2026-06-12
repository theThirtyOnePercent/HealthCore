package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jshell.Diag;

import java.time.LocalDateTime;
/**
 * @class Diagnosis
 * @brief Entity representing a medical diagnosis associated with an appointment,
 * mapped to the "diagnosis" table in the database.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name = "diagnosis")
public class Diagnosis {
    /** @brief Default constructor required by JPA. */
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
    /** @brief Default constructor required by JPA. */
    public Diagnosis(){}
    /**
     * @brief Creates a Diagnosis entry for a given appointment.
     * @param appointment           The appointment this diagnosis is associated with.
     * @param dateStartCondition   The date when the condition started.
     * @param dateEndCondition     The date when the condition ended (if applicable).
     * @param description          A detailed description of the diagnosis.
     */
    public Diagnosis(Appointment appointment, LocalDateTime dateStartCondition, LocalDateTime dateEndCondition, String description) {
        this.appointment = appointment;
        this.dateRecord = LocalDateTime.now();
        this.dateStartCondition = dateStartCondition;
        this.dateEndCondition = dateEndCondition;
        this.description = description;
    }
    /** @brief Returns the diagnosis ID. */
    public Integer getDiagnosisId() {
        return diagnosisId;
    }
    /** @brief Sets the diagnosis ID. */
    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }
    /** @brief Returns the associated appointment. */
    public Appointment getAppointment() {
        return appointment;
    }
    /** @brief Sets the associated appointment. */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    /** @brief Returns the date when the diagnosis was recorded. */
    public LocalDateTime getDateRecord() {
        return dateRecord;
    }
    /** @brief Sets the date when the diagnosis was recorded. */
    public void setDateRecord(LocalDateTime dateRecord) {
        this.dateRecord = dateRecord;
    }   
    /** @brief Returns the date when the condition started. */
    public LocalDateTime getDateStartCondition() {
        return dateStartCondition;
    }
    /** @brief Sets the Start date. */
    public void setDateStartCondition(LocalDateTime dateStartCondition) {
        this.dateStartCondition = dateStartCondition;
    }
    /** @brief Returns the End date. */
    public LocalDateTime getDateEndCondition() {
        return dateEndCondition;
    }
    /** @brief Sets the End date. */
    public void setDateEndCondition(LocalDateTime dateEndCondition) {
        this.dateEndCondition = dateEndCondition;
    }
    /** @brief Returns the diagnosis description. */
    public String getDescription() {
        return description;
    }
    /** @brief Sets the diagnosis description. */
    public void setDescription(String description) {
        this.description = description;
    }
}
