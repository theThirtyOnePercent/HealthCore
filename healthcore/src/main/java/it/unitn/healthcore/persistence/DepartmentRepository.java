package it.unitn.healthcore.persistence;


import it.unitn.healthcore.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
