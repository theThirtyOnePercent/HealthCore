package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Integer> {
}
