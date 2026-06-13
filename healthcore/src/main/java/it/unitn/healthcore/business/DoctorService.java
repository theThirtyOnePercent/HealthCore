package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
import it.unitn.healthcore.persistence.AppointmentRepository;
import it.unitn.healthcore.persistence.DepartmentRepository;
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
/** @class DoctorService
 * @brief  DoctorService is a service class that provides business logic related to doctor management and scheduling. It interacts with the DoctorRepository, ShiftRepository, UserService, and AppointmentRepository to perform operations such as retrieving doctor information, managing shifts, and handling appointments. The service ensures that doctors' schedules adhere to constraints such as maximum weekly hours and non-overlapping shifts.
 * @see DoctorRepository
 * @see ShiftRepository
 * @see UserService
 * @see AppointmentRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final ShiftRepository shiftRepository;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    /** @brief Constructor for the DoctorService class.
     * This constructor is used to inject the DoctorRepository, ShiftRepository, UserService, and AppointmentRepository dependencies into the service. The @Autowired annotation allows Spring to automatically wire the service when creating an instance of the DoctorService.
     * @param doctorRepository The repository that provides access to doctor data in the database.
     * @param shiftRepository The repository that provides access to shift data in the database.
     * @param userService The service that provides user-related functionalities such as retrieving the current user.
     * @param departmentRepository The repository that provides access to department data in the database.
     * */
    @Autowired
    public DoctorService(DoctorRepository doctorRepository, ShiftRepository shiftRepository, UserService userService, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.shiftRepository = shiftRepository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }
    /** @brief Retrieves a list of all doctors in the system.
     * This method interacts with the DoctorRepository to fetch all doctor records from the database and returns them as a list.
     * @return A list of all doctors in the system.
     */
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }
    /** @brief Retrieves detailed information about a specific doctor based on the doctor ID.
     * This method interacts with the DoctorRepository to find a doctor by their ID. If the doctor is found, it returns the Doctor object; otherwise, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param id The ID of the doctor to retrieve.
     * @return The Doctor object corresponding to the specified ID.
     * @throws ResponseStatusException if the doctor with the specified ID is not found in the database.
     */
    public Doctor findDoctor(Integer id){
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found");
        }

        return doctor.get();
    }
    /** @brief Retrieves the doctor associated with the currently authenticated user.
     * This method uses the UserService to get the current user and checks if the user is an instance of Doctor. If so, it returns the Doctor object; otherwise, it throws a ResponseStatusException with a 403 FORBIDDEN status.
     * @return The Doctor object corresponding to the currently authenticated user.
     * @throws ResponseStatusException if the current user is not a doctor or if there is an issue retrieving the doctor information.
     */
    private long getHours(LocalDateTime start,
                          LocalDateTime end) {

        return java.time.Duration
                .between(start, end)
                .toHours();
    }

    /** @brief Helper method to calculate the total number of hours a doctor is scheduled to work in a given week.
     * This method iterates through the doctor's shifts and sums the hours of those that fall within the specified week (between weekStart and weekEnd). It returns the total number of hours scheduled for that week.
     * @param doctor The doctor for whom to calculate weekly hours.
     * @param weekStart The start date and time of the week for which to calculate hours.
     * @param weekEnd The end date and time of the week for which to calculate hours.
     * @return The total number of hours the doctor is scheduled to work in the specified week.
     */
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
    /** @brief Helper method to check if a proposed shift overlaps with an existing shift for a doctor.
     * This method compares the start and end times of the proposed shift with those of an existing shift to determine if there is any overlap. It returns true if the proposed shift overlaps with the existing shift, and false otherwise.
     * @param shift The existing shift to compare against.
     * @param newStart The start date and time of the proposed new shift.
     * @param newEnd The end date and time of the proposed new shift.
     * @return true if the proposed shift overlaps with the existing shift; false otherwise.
     */
    private boolean overlaps(Shift shift,
                             LocalDateTime newStart,
                             LocalDateTime newEnd) {

        return newStart.isBefore(shift.getEndTime())
                && newEnd.isAfter(shift.getStartTime());
    }
    /** @brief Helper method to retrieve a specific shift based on its ID.
     * This method interacts with the ShiftRepository to find a shift by its ID. If the shift is found, it returns the Shift object; otherwise, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param shiftId The ID of the shift to retrieve.
     * @return The Shift object corresponding to the specified ID.
     * @throws ResponseStatusException if the shift with the specified ID is not found in the database.
     */
    private Shift findShift(Integer shiftId) {

        return shiftRepository.findById(shiftId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "shift not found"));
    }
    /** @brief Helper method to retrieve detailed information about a doctor associated with a specific appointment.
     * This method constructs a string containing the doctor's personal information, specialization, department, hospital, appointment price, shift schedule, and any notes associated with the appointment.
     * @param appointment The appointment for which to retrieve doctor details.
     * @return A string containing detailed information about the doctor associated with the specified appointment.
     */
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
    /** @brief Helper method to delete a specific shift based on its ID.
     * This method interacts with the ShiftRepository to find and delete a shift by its ID. If the shift is found, it is deleted from the database; otherwise, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param shiftId The ID of the shift to delete.
     * @throws ResponseStatusException if the shift with the specified ID is not found in the database.
     */
    @Transactional
    public void deleteShift(Integer shiftId) {

        Shift shift = findShift(shiftId);

        Doctor doctor = shift.getDoctor();

        shiftRepository.delete(shift);
        shiftRepository.flush();
        doctorRepository.flush();
    }
    /** @brief Helper method to retrieve detailed information about a doctor associated with a specific appointment.
     * This method constructs a string containing the doctor's personal information, specialization, department, hospital, appointment price, shift schedule, and any notes associated with the appointment.
     * @param appointment The appointment for which to retrieve doctor details.
     * @return A string containing detailed information about the doctor associated with the specified appointment.
     */
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
    /** @brief Retrieves the shifts of the currently authenticated doctor.
     * This method uses the UserService to get the current user and checks if the user is an instance of Doctor. If so, it retrieves and returns a list of the doctor's shifts sorted by start time. If the current user is not a doctor, it throws a ResponseStatusException with a 403 FORBIDDEN status.
     * @return A list of shifts for the currently authenticated doctor, sorted by start time.
     * @throws ResponseStatusException if the current user is not a doctor or if there is an issue retrieving the doctor information.
     */
    public List<Shift> getCurrentDoctorShifts() {
        Doctor doctor = (Doctor) userService.getCurrentUser();
        return doctor.getShifts().stream()
                .sorted(java.util.Comparator.comparing(Shift::getStartTime))
                .toList();
    }
    /** @brief Retrieves a list of departments that have available staff positions for new doctors.
     * This method interacts with the DepartmentRepository to fetch all department records from the database.
     *  It then filters the list to include only those departments that have available staff positions (i.e., the number of occupied positions is less than the total staff positions). The filtered list of available departments is returned.
     * @return A list of departments that have available staff positions for new doctors.
     */
    public List<Department> getAvailableDepartments(){
        List<Department> departments = departmentRepository.findAll();

        for (Department department: departments){
            // Check if department has a free position for a potential new doctor
            int occupiedPositions = department.getDoctors().size();

            if (occupiedPositions >= department.getTotalStaffPositions()) {
               departments.remove(department);
            }
        }

        return departments;
    }
    /**
     * @brief Changes the department of a doctor to a new department.
     * This method retrieves the doctor and the new department based on their IDs. It checks if the new department has available staff positions. If the department is full, it throws a ResponseStatusException with a 400 BAD REQUEST status. 
     * If the department has available positions, it updates the doctor's department
     * The method is annotated with @Transactional to ensure that the changes are persisted in the database.
     * @param doctorId The ID of the doctor whose department is to be changed.
     * @param departmentId The ID of the new department to which the doctor will be assigned.
     * @throws ResponseStatusException if the doctor or department is not found, or if the new department has no available staff positions.
     */
    @Transactional
    public void changeDepartment(Integer doctorId, Integer departmentId){
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "doctor not found"));

        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        // Check if department has a free position for the new doctor
        int occupiedPositions = department.getDoctors().size();

        if (occupiedPositions >= department.getTotalStaffPositions()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department has no available staff positions");
        }

        doctor.setDepartment(department);
    }
}
