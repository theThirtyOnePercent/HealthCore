package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * @class Note
 * @brief Entity representing a note associated with an appointment, mapped to the "Notes" table.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name = "Notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noteId;

    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    @Lob
    private String content;

    private LocalDateTime sentDate;

    /** @brief Default constructor required by JPA. */
    public Note(){}

    /**
     * @brief Creates a Note for a given appointment.
     * @param appointment The appointment this note is associated with.
     * @param content     The content of the note.
     */
    public Note(Appointment appointment, String content) {
        this.appointment = appointment;
        this.content = content;
        this.sentDate = LocalDateTime.now();
    }

    /** @brief Returns the associated appointment. */
    public Appointment getAppointment() {
        return appointment;
    }

    /** @brief Sets the associated appointment. */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
     /** @brief Returns the note ID. */
    public Integer getNoteId() {
        return noteId;
    }

    /** @brief Sets the note ID. */
    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    /** @brief Returns the content of the note. */
    public String getContent() {
        return content;
    }

    /** @brief Sets the content of the note. */
    public void setContent(String content) {
        this.content = content;
    }

        /** @brief Returns the date and time when the note was sent. */
    public LocalDateTime getSentDate() {
        return sentDate;
    }

    /** @brief Sets the date and time when the note was sent. */
    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}
