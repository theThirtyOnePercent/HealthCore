package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * @class EquipmentDoctor
 * @brief Entity representing a piece of equipment assigned to a Department,
 * mapped to the "equipments_doctors" table.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Entity
@Table(name = "equipmentsdoctors")
public class EquipmentDoctor {
    /** @brief Default constructor required by JPA. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @JsonIgnore
    private Doctor doctor;

    private Integer quantity;
    /** @brief Default constructor required by JPA. */
    public EquipmentDoctor(){}

    /**
     * @brief Create
     * @param equipment    The department this equipment belongs to.
     * @param doctor    The doctor conducting the appointment.
     * @param quantity      The available quantity.
     */
    public EquipmentDoctor(Equipment equipment, Doctor doctor, Integer quantity) {
        this.equipment = equipment;
        this.doctor = doctor;
        this.quantity = quantity;
    }
    /** @brief Returns the equipment ID. */
    public Integer getId() {
        return id;
    }

    /** @brief Sets the equipment ID. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @brief Getter for equipment. */
    public Equipment getEquipment() {
        return equipment;
    }
    /** @brief Setter for equipment. */
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
    /** @brief Getter for doctor. */
    public Doctor getDoctor() {
        return doctor;
    }

    /** @brief Setter for doctor. */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /** @brief Getter for quantity. */
    public Integer getQuantity() {
        return quantity;
    }
 
    /** @brief Setter for quantity. */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
