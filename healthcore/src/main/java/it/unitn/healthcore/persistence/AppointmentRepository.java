package it.unitn.healthcore.persistence;

import it.unitn.healthcore.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorId(Integer doctorId);
    List<Appointment> findByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Integer doctorId,
            LocalDateTime end,
            LocalDateTime start
    );
}
