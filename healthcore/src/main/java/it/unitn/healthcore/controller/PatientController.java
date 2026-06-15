package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.AppointmentService;
import it.unitn.healthcore.business.PatientService;
import it.unitn.healthcore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @class PatientController
 * @brief  PatientController is a REST controller that handles HTTP requests related to patient management and appointment booking for patients. It provides endpoints for patients to manage their insurance plans, view doctor descriptions, book appointments with doctors, and view their appointment history and details.
 * @see PatientService
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@RestController
@RequestMapping(path = "patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    /** @brief Constructor for the PatientController class.
     * This constructor is used to inject the PatientService and AppointmentService dependencies into the controller.
     * The @Autowired annotation allows Spring to automatically wire the services when creating an instance of the controller.
     * @param patientService The service that provides business logic related to patients.
     * @param appointmentService The service that provides business logic related to appointments.
     */
    @Autowired
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }
    /** @brief Retrieves the current insurance plan of the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It returns the InsurancePlan object representing the patient's current insurance plan.
     * @return The current InsurancePlan of the patient.
     */
    @PostMapping(path = "updateInsurance")
    public InsurancePlan updateInsurancePlan(@RequestParam Integer planId){
        patientService.updateInsurancePlan(planId);
        return patientService.getCurrentInsurancePlan();
    }
    /** @brief Returns the description of a specific doctor by their ID.
      * This endpoint is accessible only to users with the 'PATIENT' role.
      * It returns a string containing the doctor's description, which may include information.
      * @param doctorId The ID of the doctor whose description is to be retrieved.
      * @return A string containing the doctor's description.
      */
    @GetMapping(path = "doctorDescription/{doctorId}")
    public String getDoctorDescription(
            @PathVariable("doctorId") Integer doctorId) {

        return patientService.getDoctorDescription(doctorId);
    }
    /** @brief Books an appointment with a specified doctor for the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It takes the doctor's ID, start time, and end time as parameters and creates an appointment.
     * It returns a string containing the details of the created appointment, including doctor information, department, and hospital.
     * @param doctorId The ID of the doctor to book an appointment with.
     * @param startTime The start time of the appointment.
     * @param endTime The end time of the appointment.
     * @return A string containing the details of the booked appointment.
     */
    @PostMapping(path = "book/{doctorId}")
    public String bookAppointment(@PathVariable Integer doctorId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {

        Appointment appointment =
                patientService.bookAppointment(doctorId, startTime, endTime);

        Doctor doctor = appointment.getDoctor();
        Department dept = doctor.getDepartment();
        Hospital hospital = dept.getHospital();

        StringBuilder sb = new StringBuilder();

        sb.append("Appointment created\n")
                .append("Appointment ID: ").append(appointment.getAppointmentId()).append("\n")
                .append("Start: ").append(appointment.getStartTime()).append("\n")
                .append("End: ").append(appointment.getEndTime()).append("\n\n")

                .append("Doctor: ")
                .append(doctor.getName()).append(" ")
                .append(doctor.getSurname()).append("\n")
                .append("Email: ").append(doctor.getEmail()).append("\n")

                .append("Department: ").append(dept.getName()).append("\n")
                .append("Hospital: ").append(hospital.getName()).append("\n");

        return sb.toString();
    }
    /** @brief Retrieves a list of future appointments for the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It returns a string representation of each future appointment, including the appointment ID, start and end times, and doctor information.
     * @return A string containing the details of future appointments for the patient.
     */
    @GetMapping (path = "appointments")
    public String viewFutureAppointments(){
        List<Appointment> appointments = appointmentService.getFutureAppointments();

        StringBuilder sb = new StringBuilder("Next Appointments:\n");

        for (Appointment a : appointments) {
            sb.append("ID: ").append(a.getAppointmentId())
                    .append(", Start: ").append(a.getStartTime())
                    .append(", End: ").append(a.getEndTime())
                    .append("Doctor: ").append(a.getDoctor())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Retrieves a list of past appointments for the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It returns a string representation of each past appointment, including the appointment ID, start and end times, and doctor information.
     * @return A string containing the details of past appointments for the patient.
     */
    @GetMapping (path = "appointments/history")
    public String viewPastAppointments(){
        List<Appointment> appointments = appointmentService.getPastAppointments();

        StringBuilder sb = new StringBuilder("Appointment history:\n");

        for (Appointment a : appointments) {
            sb.append("ID: ").append(a.getAppointmentId())
                    .append(", Start: ").append(a.getStartTime())
                    .append(", End: ").append(a.getEndTime())
                    .append("Doctor: ").append(a.getDoctor())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Retrieves the details of a specific appointment by its ID.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It returns a string containing the details of the appointment, including doctor information, diagnosis history, and any notes associated with the appointment.
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A string containing the details of the specified appointment.
     */
    @GetMapping(path = "appointment/detail/{appointmentId}")
    public String viewAppointmentDetail(@PathVariable Integer appointmentId){
        return appointmentService.getAppointmentDetails(appointmentId);
    }
    /** @brief Modifies the details of an existing appointment for the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It takes the appointment ID, new start time, and new end time as parameters and updates the appointment accordingly.
     * It does not return any content in the response.
     * @param appointmentId The ID of the appointment to modify.
     * @param startTime The new start time for the appointment.
     * @param endTime The new end time for the appointment.
     */
    @PostMapping(path = "appointment/modify/{appointmentId}")
    public void modifyAppointment (@PathVariable Integer appointmentId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){
        patientService.modifyAppointment(appointmentId, startTime, endTime);
    }

    /** @brief Cancels an existing appointment for the authenticated patient.
     * This endpoint is accessible only to users with the 'PATIENT' role.
     * It takes the appointment ID as a parameter and cancels the corresponding appointment.
     * It does not return any content in the response.
     * @param appointmentId The ID of the appointment to cancel.
     */
    @PostMapping(path = "appointment/cancel/{appointmentId}")
    public void cancelAppointment (@PathVariable Integer appointmentId){
        patientService.cancelAppointment(appointmentId);
    }

}
