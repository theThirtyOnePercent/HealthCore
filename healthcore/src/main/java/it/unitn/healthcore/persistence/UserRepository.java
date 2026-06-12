package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @interface UserRepository
 * @brief Repository interface for managing User entities in the database.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 * This interface is used by the service layer to interact with the persistence layer.
 * @see JpaRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.email = ?1")
    Optional<User> findUserByEmail(String email);
}
