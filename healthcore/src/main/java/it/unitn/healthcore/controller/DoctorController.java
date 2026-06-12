package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.AppointmentService;
import it.unitn.healthcore.business.DoctorService;
import it.unitn.healthcore.domain.Appointment;
import it.unitn.healthcore.domain.Doctor;
import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.domain.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @class doctorController
 * @brief  DoctorController is a REST controller that handles HTTP requests related to doctor management and appointment viewing for doctors. It provides endpoints for doctor to manage doctor shifts and for doctors to view their appointments and shift schedules.
 * @see 
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@RestController
@RequestMapping(path = "doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    /** @brief Constructor for the DoctorController class.
     * This constructor is used to inject the DoctorService and AppointmentService dependencies into the controller.
     * The @Autowired annotation allows Spring to automatically wire the services when creating an instance of the controller.
     * @param doctorService The service that provides business logic related to doctors.
     * @param appointmentService The service that provides business logic related to appointments.
     */
    @Autowired
    public DoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }
    /** @brief Retrieves a list of all doctors in the system.
     * This endpoint is accessible to users with the 'ADMINISTRATOR' or 'PATIENT' role.
     * It returns a string representation of each doctor's ID, name, surname, and email.
     * @return A string containing the details of all doctors.
     */
    @GetMapping(path = "list")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('PATIENT')")
    public String viewDoctors(){
        StringBuilder sb = new StringBuilder();

        for (Doctor d : doctorService.getAllDoctors()) {
            sb.append("ID: ").append(d.getId())
                    .append(", Name: ").append(d.getName())
                    .append(", Surname: ").append(d.getSurname())
                    .append(", Email: ").append(d.getEmail())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Retrieves the details of a specific doctor by their ID.
     * This endpoint is accessible only to users with the 'ADMINISTRATOR' role.
     * It returns a Doctor object containing the doctor's details.
     * @param id The ID of the doctor to retrieve.
     * @return A Doctor object with the specified ID.
     */
    @GetMapping(path = "management/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Doctor viewDoctorManagementPage (@PathVariable("userId") Integer id){
        return doctorService.findDoctor(id);
    }
    /** @brief Adds a new shift for a doctor.
     * This endpoint is accessible only to users with the 'ADMINISTRATOR' role.
     * It takes the doctor's ID, the start time, and the end time of the shift as parameters.
     * The method calls the DoctorService to add the shift to the specified doctor.
     * @param id The ID of the doctor for whom to add the shift.
     * @param startTime The start time of the shift.
     * @param endTime The end time of the shift.
     */
    @PostMapping(path = "addShift/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addShiftTime (@PathVariable("userId") Integer id, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){

        doctorService.addShift(id, startTime, endTime);
    }
    /** @brief Modifies an existing shift for a doctor.
     * This endpoint is accessible only to users with the 'ADMINISTRATOR' role.
     * It takes the shift's ID, the new start time, and the new end time as parameters.
     * The method calls the DoctorService to update the specified shift with the new times.
     * @param shiftId The ID of the shift to modify.
     * @param startTime The new start time of the shift.
     * @param endTime The new end time of the shift.
     */
    @PutMapping(path = "modifyShift/{shiftId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void modifyShift(@PathVariable Integer shiftId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {

        doctorService.modifyShift(shiftId, startTime, endTime);
    }
    /** @brief Deletes an existing shift for a doctor.
     * This endpoint is accessible only to users with the 'ADMINISTRATOR' role.
     * It takes the shift's ID as a parameter and calls the DoctorService to remove the specified shift from the system.
     * @param shiftId The ID of the shift to delete.
     */
    @DeleteMapping(path = "deleteShift/{shiftId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void deleteShift(@PathVariable Integer shiftId) {

        doctorService.deleteShift(shiftId);
    }
    /** @brief Retrieves the details of a specific appointment by its ID.
     * This endpoint is accessible only to users with the 'DOCTOR' role.
     * It returns a string containing the details of the appointment, including patient information, diagnosis history, and any notes associated with the appointment.
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A string containing the details of the specified appointment.
     */
    @GetMapping(path = "shifts")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<Shift> viewMyShifts() {

        return doctorService.getCurrentDoctorShifts();
    }

    /** @brief Retrieves a list of future appointments for the currently authenticated doctor.
     * This endpoint is accessible only to users with the 'DOCTOR' role.
     * It returns a string representation of each future appointment, including the appointment ID, start and end times, and patient information.
     * @return A string containing the details of future appointments for the doctor.
     */
    @GetMapping(path = "appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public String viewFutureAppointments() {

        List<Appointment> appointments = appointmentService.getFutureAppointments();

        StringBuilder sb = new StringBuilder("Next Appointments:\n");

        for (Appointment a : appointments) {
            sb.append("ID: ").append(a.getAppointmentId())
                    .append(", Start: ").append(a.getStartTime())
                    .append(", End: ").append(a.getEndTime())
                    .append("\nPatient: ").append(a.getPatient().toString())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Retrieves a list of past appointments for the currently authenticated doctor.
     * This endpoint is accessible only to users with the 'DOCTOR' role.
     * It returns a string representation of each past appointment, including the appointment ID, start and end times, and patient information.
     * @return A string containing the details of past appointments for the doctor.
     */
    @GetMapping(path = "appointments/history")
    @PreAuthorize("hasRole('DOCTOR')")
    public String viewPastAppointments() {

        List<Appointment> appointments = appointmentService.getPastAppointments();

        StringBuilder sb = new StringBuilder("Past Appointments:\n");

        for (Appointment a : appointments) {
            sb.append("ID: ").append(a.getAppointmentId())
                    .append(", Start: ").append(a.getStartTime())
                    .append(", End: ").append(a.getEndTime())
                    .append("Patient: ").append(a.getPatient())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Retrieves the details of a specific appointment by its ID.
     * This endpoint is accessible only to users with the 'DOCTOR' role.
     * It returns a string containing the details of the appointment, including patient information, diagnosis history, and any notes associated with the appointment.
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A string containing the details of the specified appointment.
     */
    @GetMapping(path = "appointment/detail/{appointmentId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public String viewAppointmentDetail(@PathVariable Integer appointmentId){
        return appointmentService.getAppointmentDetails(appointmentId);
    }

}
