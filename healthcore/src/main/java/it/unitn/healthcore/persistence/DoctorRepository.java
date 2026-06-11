package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
