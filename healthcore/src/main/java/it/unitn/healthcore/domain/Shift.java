package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * @class Shift
 * @brief Entity representing a work shift for a doctor, mapped to the "shifts" table.
 * @detail Each Shift is associated with a Doctor and has a start and end time, indicating when the doctor is scheduled to work.
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table (name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftId;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private Doctor doctor;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /** @brief Default constructor required by JPA. */
    public Shift(){}

    /**
     * @brief Creates a Shift for a given doctor and time period.
     * @param doctor    The doctor assigned to the shift.
     * @param startTime The start date and time of the shift.
     * @param endTime   The end date and time of the shift.
     */
    public Shift(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /** @brief Returns the shift ID. */
    public Integer getShiftId() {
        return shiftId;
    }
    /** @brief Sets the shift ID. */
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }
    /** @brief Returns the doctor assigned to the shift. */
    public Doctor getDoctor() {
        return doctor;
    }
    /** @brief Sets the doctor assigned to the shift. */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    /** @brief Returns the start time of the shift. */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    /** @brief Sets the start time of the shift. */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    /** @brief Returns the end time of the shift. */
    public LocalDateTime getEndTime() {
        return endTime;
    }
    /** @brief Sets the end time of the shift. */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
