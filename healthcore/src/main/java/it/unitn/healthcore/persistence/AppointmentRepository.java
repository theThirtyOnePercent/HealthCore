package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @interface AppointmentRepository
 * @brief Repository interface for managing Appointment entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

}
