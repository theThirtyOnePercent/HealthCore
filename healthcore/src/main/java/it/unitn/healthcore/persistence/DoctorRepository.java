package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface DoctorRepository
 * @brief Repository interface for managing Doctor entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

}
