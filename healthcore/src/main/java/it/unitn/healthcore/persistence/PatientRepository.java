package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @interface PatientRepository
 * @brief Repository interface for managing Patient entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    /** @brief Checks if a patient exists in the database by their healthcare card number. */
    boolean existsByHealthcareCardNumber(Integer healthcareCardNumber);
}
