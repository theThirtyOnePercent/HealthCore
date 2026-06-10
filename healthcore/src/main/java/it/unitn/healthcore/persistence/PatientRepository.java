package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    boolean existsByHealthcareCardNumber(Integer healthcareCardNumber);
}
