package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
}
