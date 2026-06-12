package it.unitn.healthcore.persistence;


import it.unitn.healthcore.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @interface DepartmentRepository
 * @brief Repository interface for managing Department entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
