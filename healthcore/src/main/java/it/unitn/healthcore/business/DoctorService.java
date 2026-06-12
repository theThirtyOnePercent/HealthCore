package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.Appointment;
import it.unitn.healthcore.domain.Doctor;
import it.unitn.healthcore.domain.Shift;
import it.unitn.healthcore.persistence.DoctorRepository;
import it.unitn.healthcore.persistence.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ShiftRepository shiftRepository;
    private final UserService userService;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, ShiftRepository shiftRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.shiftRepository = shiftRepository;
        this.userService = userService;
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor findDoctor(Integer id){
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        }

        return doctor.get();
    }

    private long getHours(LocalDateTime start,
                          LocalDateTime end) {

        return java.time.Duration
                .between(start, end)
                .toHours();
    }

    private long calculateWeeklyHours(Doctor doctor,
                                      LocalDateTime weekStart,
                                      LocalDateTime weekEnd) {

        long total = 0;

        for (Shift shift : doctor.getShifts()) {

            if (!shift.getStartTime().isBefore(weekStart)
                    && !shift.getEndTime().isAfter(weekEnd)) {

                total += getHours(shift.getStartTime(), shift.getEndTime());
            }
        }

        return total;
    }

    private boolean overlaps(Shift shift,
                             LocalDateTime newStart,
                             LocalDateTime newEnd) {

        return newStart.isBefore(shift.getEndTime())
                && newEnd.isAfter(shift.getStartTime());
    }

    private Shift findShift(Integer shiftId) {

        return shiftRepository.findById(shiftId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "shift not found"));
    }

    @Transactional
    public void addShift(Integer id, LocalDateTime startTime, LocalDateTime endTime) {

        Doctor doctor = findDoctor(id);

        if (!startTime.isBefore(endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift start must be before shift end");
        }

        // overlap check
        for (Shift shift : doctor.getShifts()) {
            if (overlaps(shift, startTime, endTime)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift overlaps an existing shift");
            }
        }

        // week limits
        LocalDateTime weekStart = startTime.toLocalDate().minusDays(startTime.getDayOfWeek().getValue() - 1).atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusDays(7);

        long currentHours = calculateWeeklyHours(doctor, weekStart, weekEnd);
        long newShiftHours = getHours(startTime, endTime);

        if (currentHours + newShiftHours > 48) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor exceeds 48 weekly hours");
        }

        Shift shift = new Shift(doctor, startTime, endTime);

        shiftRepository.save(shift);
        shiftRepository.saveAndFlush(shift);
    }

    @Transactional
    public void deleteShift(Integer shiftId) {

        Shift shift = findShift(shiftId);

        Doctor doctor = shift.getDoctor();

        shiftRepository.delete(shift);
        shiftRepository.flush();
        doctorRepository.flush();
    }

    @Transactional
    public void modifyShift(Integer shiftId, LocalDateTime startTime, LocalDateTime endTime) {

        Shift shift = findShift(shiftId);
        Doctor doctor = shift.getDoctor();

        if (!startTime.isBefore(endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift start must be before shift end");
        }

        // overlap check
        for (Shift existing : doctor.getShifts()) {
            // skip the shift being modified
            if (existing.getShiftId().equals(shiftId)) {
                continue;
            }
            if (overlaps(existing, startTime, endTime)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Shift overlaps an existing shift");
            }
        }

        LocalDateTime weekStart = startTime.toLocalDate().minusDays(startTime.getDayOfWeek().getValue() - 1).atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusDays(7);

        long currentHours = calculateWeeklyHours(doctor, weekStart, weekEnd);

        long oldHours = getHours(shift.getStartTime(), shift.getEndTime());

        long newHours = getHours(
                startTime,
                endTime);

        if (currentHours - oldHours + newHours > 48) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor exceeds 48 weekly hours");}

        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        shiftRepository.saveAndFlush(shift);
    }

    public List<Shift> getCurrentDoctorShifts() {
        Doctor doctor = (Doctor) userService.getCurrentUser();
        return doctor.getShifts().stream()
                .sorted(java.util.Comparator.comparing(Shift::getStartTime))
                .toList();
    }

    public List<Appointment> getFutureAppointments() {
        Doctor doctor = (Doctor) userService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();

        return doctor.getAppointments().stream()
                .filter(a -> a.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .toList();
    }

    public List<Appointment> getPastAppointments() {
        Doctor doctor = (Doctor) userService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();

        return doctor.getAppointments()
                .stream()
                .filter(a -> a.getEndTime().isBefore(now))
                .sorted(Comparator.comparing(Appointment::getStartTime).reversed())
                .toList();
    }

}
