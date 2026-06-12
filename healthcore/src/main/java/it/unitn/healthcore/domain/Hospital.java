package it.unitn.healthcore.domain;

import jakarta.persistence.*;

import java.util.List;
/**
 * @class Hospital
 * @brief Entity representing a hospital, mapped to the "hospitals" table.
 * Holds contact details and maintains relationships with Departments and InsurancePlans.
 *
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */

@Entity
@Table(name = "hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hospitalId;
    private String name;
    private String address;
    private String phoneNumber;
    @OneToMany(mappedBy = "hospital")
    private List<Department> departmentList;
    @ManyToMany (mappedBy = "hospitals")
    private List<InsurancePlan> insurancePlans;

    /** @brief Default constructor required by JPA. */
    public Hospital(){}

    /**
     * @brief Creates a Hospital with basic contact information.
     * @param name        The hospital name.
     * @param address     The hospital address.
     * @param phoneNumber The hospital phone number.
     */
    public Hospital(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    /** @brief Returns the hospital ID. */
    public List<Department> getDepartmentList() {
        return departmentList;
    }

    /** @brief Returns the hospital ID. */
    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    /** @brief Returns the list of insurance plans associated with the hospital. */
    public List<InsurancePlan> getInsurancePlans() {
        return insurancePlans;
    }

    /** @brief Sets the list of insurance plans associated with the hospital. */
    public void setInsurancePlans(List<InsurancePlan> insurancePlans) {
        this.insurancePlans = insurancePlans;
    }

    /** @brief Returns the hospital ID. */
    public Integer getHospitalId() {
        return hospitalId;
    }

    /** @brief Sets the hospital ID. */
    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    /** @brief Returns the hospital name. */
    public String getName() {
        return name;
    }

    /** @brief Sets the hospital name. */
    public void setName(String name) {
        this.name = name;
    }

    /** @brief Returns the hospital address. */
    public String getAddress() {
        return address;
    }

    /** @brief Sets the hospital address. */
    public void setAddress(String address) {
        this.address = address;
    }

        /** @brief Returns the hospital phone number. */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** @brief Sets the hospital phone number. */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
