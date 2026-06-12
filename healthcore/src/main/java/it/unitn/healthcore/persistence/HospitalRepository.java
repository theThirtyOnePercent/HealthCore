package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface HospitalRepository
 * @brief Repository interface for managing Hospital entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
}
