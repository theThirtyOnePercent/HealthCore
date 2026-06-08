package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.email = ?1")
    Optional<User> findUserByEmail(String email);
}
