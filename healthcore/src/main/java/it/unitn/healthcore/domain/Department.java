package it.unitn.healthcore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    @JsonIgnore
    private Hospital hospital;
    @OneToMany (mappedBy = "department")
    private List<Equipment> equipments;

    private String name;
    private Integer beds;
    private Integer totalStaffPositions;

    public Department(){}

    public Department(Hospital hospital, String name, Integer beds, Integer totalStaffPositions) {
        this.hospital = hospital;
        this.name = name;
        this.beds = beds;
        this.totalStaffPositions = totalStaffPositions;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getTotalStaffPositions() {
        return totalStaffPositions;
    }

    public void setTotalStaffPositions(Integer totalStaffPositions) {
        this.totalStaffPositions = totalStaffPositions;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
