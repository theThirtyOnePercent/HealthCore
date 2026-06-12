package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Shift;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @interface ShiftRepository
 * @brief Repository interface for managing Shift entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
}
