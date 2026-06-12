package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * @class Equipment
 * @brief Entity representing a piece of equipment assigned to a Department,
 * mapped to the "equipments" table.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */

@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equipmentId;
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;
    private String equipmentType;
    private Integer quantity;
    /** @brief Default constructor required by JPA. */
    public Equipment(){}

    /**
     * @brief Creates an Equipment entry for a given department.
     * @param department    The department this equipment belongs to.
     * @param equipmentType The type/category of the equipment.
     * @param quantity      The available quantity.
     */
    public Equipment(Department department, String equipmentType, Integer quantity) {
        this.department = department;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
    }

    /** @brief Returns the equipment ID. */
    public Integer getEquipmentId() {
        return equipmentId;
    }

    /** @brief Sets the equipment ID. */
    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

     /** @brief Returns the associated department. */
    public Department getDepartment() {
        return department;
    }

    /** @brief Sets the associated department. */
    public void setDepartment(Department department) {
        this.department = department;
    }
    /** @brief Returns the equipment type. */
    public String getEquipmentType() {
        return equipmentType;
    }
      /** @brief Sets the equipment type. */
    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    /** @brief Returns the quantity. */
    public Integer getQuantity() {
        return quantity;
    }

    /** @brief Sets the quantity. */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
