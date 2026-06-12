package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @class Appointment
 * @brief Entity representing a scheduled appointment between a Patient and a Doctor,
 * mapped to the "appointments" table.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "start_date")
    private LocalDateTime startTime;
    @Column(name = "end_date")
    private LocalDateTime endTime;

    @OneToMany (mappedBy = "appointment")
    @JsonIgnore
    private List<Diagnosis> diagnoses;

    @OneToOne (mappedBy = "appointment")
    private Note note;

    public Appointment(){}

    /**
     * @brief Creates an Appointment between a patient and a doctor.
     * @param patient   The patient attending the appointment.
     * @param doctor    The doctor conducting the appointment.
     * @param startTime Appointment start date and time.
     * @param endTime   Appointment end date and time.
     */
    public Appointment(Patient patient, Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    /** @brief Returns the appointment ID. */
    public Note getNote() {
        return note;
    }
    /** @brief Sets the note associated with the appointment. */
    public void setNote(Note note) {
        this.note = note;
    }
    /** @brief Returns the list of diagnoses associated with the appointment. */
    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }
    /** @brief Sets the list of diagnoses associated with the appointment. */
    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }
    /** @brief Returns the appointment ID. */
    public Integer getAppointmentId() {
        return appointmentId;
    }
    /** @brief Sets the appointment ID. */
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
    /** @brief Returns the patient associated with the appointment. */
    public Patient getPatient() {
        return patient;
    }
    /** @brief Sets the patient associated with the appointment. */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    /** @brief Returns the doctor conducting the appointment. */
    public Doctor getDoctor() {
        return doctor;
    }
    /** @brief Sets the doctor conducting the appointment. */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    /** @brief Returns the end date and time of the appointment. */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    /** @brief Sets the end date and time of the appointment. */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    /** @brief Returns the start date and time of the appointment. */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    /** @brief Sets the start date and time of the appointment. */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
