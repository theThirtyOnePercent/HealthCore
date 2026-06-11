package it.unitn.healthcore.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="doctors")
@DiscriminatorValue("Doctor")
public class Doctor extends User{
    private Integer departmentId;
    private String specialization;
    private Double appointmentPrice;
    @OneToMany(mappedBy = "doctor")
    private List<EquipmentDoctor> equipments;
    @OneToMany (mappedBy = "doctor", fetch = FetchType.EAGER)
    private List<Shift> shifts;

    public Doctor(){

    }

    public Doctor(String name, String surname, String email, String password, Integer departmentId, String specialization, Double appointmentPrice) {
        super(name, surname, email, password);
        this.departmentId = departmentId;
        this.specialization = specialization;
        this.appointmentPrice = appointmentPrice;
    }

    public Doctor(String name, String surname, String email, String password, Integer departmentId) {
        super(name, surname, email, password);
        this.departmentId = departmentId;
        this.specialization = null;
        this.appointmentPrice = null;
    }

    public List<EquipmentDoctor> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentDoctor> equipments) {
        this.equipments = equipments;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Double getAppointmentPrice() {
        return appointmentPrice;
    }

    public void setAppointmentPrice(Double appointmentPrice) {
        this.appointmentPrice = appointmentPrice;
    }
}

