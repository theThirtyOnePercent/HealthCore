package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Shift;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
}
