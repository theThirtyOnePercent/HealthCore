package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
import it.unitn.healthcore.persistence.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
/**
 * @class AppointmentService
 * @brief  AppointmentService is a service class that provides business logic related to appointments.
 *  It interacts with the UserService to retrieve the current user and the AppointmentRepository to access appointment data. 
 * The service includes methods for retrieving future and past appointments, as well as getting detailed information about specific appointments.
 * @detail The AppointmentService class is responsible for handling appointment-related operations in the system. It provides methods to retrieve
 *  future and past appointments for the currently authenticated user, as well as detailed information about specific appointments. The service ensures that users can only 
 *  access appointments they are assigned to, and it formats the appointment details for display.
 * @see AppointmentRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Service
public class AppointmentService {
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    /** @brief Constructor for the AppointmentService class.
     * This constructor is used to inject the UserService and AppointmentRepository dependencies into the service.
     * The @Autowired annotation allows Spring to automatically wire the service when creating an instance of the AppointmentService.
     * @param userService The service that provides user-related functionalities such as retrieving the current user.
     * @param appointmentRepository The repository that provides access to appointment data in the database.
     * */
    @Autowired
    public AppointmentService(UserService userService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }
    /** @brief Retrieves a list of future appointments for the currently authenticated user.
     * This method checks the type of the current user (Doctor or Patient) and retrieves their respective appointments. 
     * It then filters the appointments to include only those that are scheduled for the future (i.e., start time is after the current time) and sorts them by start time in ascending order.
     * @return A list of future appointments for the current user, sorted by start time.
     */
    public List<Appointment> getFutureAppointments() {

        User user = userService.getCurrentUser();

        List<Appointment> appointments;

        if (user instanceof Doctor doctor) {
            appointments = doctor.getAppointments();
        }
        else if (user instanceof Patient patient) {
            appointments = patient.getAppointments();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported user type"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        return appointments.stream()
                .filter(a -> a.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .toList();
    }
    /** @brief Retrieves a list of past appointments for the currently authenticated user.
     * This method checks the type of the current user (Doctor or Patient) and retrieves their respective appointments. 
     * It then filters the appointments to include only those that have already occurred (i.e., end time is before the current time) and sorts them by start time in descending order.
     * @return A list of past appointments for the current user, sorted by start time in descending order.
     */
    public List<Appointment> getPastAppointments() {

        User user = userService.getCurrentUser();

        List<Appointment> appointments;

        if (user instanceof Doctor doctor) {
            appointments = doctor.getAppointments();
        }
        else if (user instanceof Patient patient) {
            appointments = patient.getAppointments();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported user type");
        }

        LocalDateTime now = LocalDateTime.now();

        return appointments.stream()
                .filter(a -> a.getEndTime().isBefore(now))
                .sorted(Comparator.comparing(Appointment::getStartTime).reversed())
                .toList();
    }
    /** @brief Retrieves detailed information about a specific appointment based on the appointment ID.
     * This method checks the type of the current user (Doctor or Patient) and verifies that they are assigned to the requested appointment. 
     * If the user is authorized to view the appointment, it returns a string containing detailed information about the appointment, including patient or doctor details and any associated notes or diagnoses.
     * @param appointmentId The ID of the appointment for which to retrieve details.
     * @return A string containing detailed information about the specified appointment.
     */
    public String getAppointmentDetails(Integer appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        StringBuilder sb = new StringBuilder();

        sb.append("Appointment ID: ")
                .append(appointment.getAppointmentId())
                .append("\n");

        sb.append("Start: ")
                .append(appointment.getStartTime())
                .append("\n");

        sb.append("End: ")
                .append(appointment.getEndTime())
                .append("\n\n");

        User user = userService.getCurrentUser();
        if (user instanceof Doctor doctor) {
            if (!appointment.getDoctor().getId().equals(doctor.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "You are not assigned to this appointment");
            }
            sb.append(getPatientDetails(appointment));
        }
        else if (user instanceof Patient patient) {
            if (!appointment.getPatient().getId().equals(patient.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "You are not assigned to this appointment");
            }
            sb.append(getDoctorDetails(appointment));
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported user type");
        }

        return sb.toString();

    }
    /** @brief Helper method to retrieve detailed information about the patient associated with a specific appointment.
     * This method constructs a string containing the patient's personal information, triage status, insurance plan (if available), diagnosis history, and any notes associated with the appointment.
     * @param appointment The appointment for which to retrieve patient details.
     * @return A string containing detailed information about the patient associated with the specified appointment.
     */
    private String getPatientDetails(Appointment appointment){
        StringBuilder sb = new StringBuilder();

        Patient patient = appointment.getPatient();

        sb.append("Patient Information\n");
        sb.append("-------------------\n");

        sb.append("Patient ID: ").append(patient.getId()).append("\n");
        sb.append("Name: ").append(patient.getName()).append("\n");
        sb.append("Surname: ").append(patient.getSurname()).append("\n");
        sb.append("Email: ").append(patient.getEmail()).append("\n");
        sb.append("Triage Status: ").append(patient.getTriageStatus()).append("\n");

        if (patient.getInsurancePlan() != null) {
            sb.append("Insurance Plan: ")
                    .append(patient.getInsurancePlan().getName())
                    .append("\n");
        }

        sb.append("\nDiagnosis History\n");
        sb.append("-----------------\n");

        boolean foundDiagnosis = false;

        for (Appointment patientAppointment : patient.getAppointments()) {

            for (Diagnosis diagnosis : patientAppointment.getDiagnoses()) {

                foundDiagnosis = true;

                sb.append("Diagnosis ID: ")
                        .append(diagnosis.getDiagnosisId())
                        .append("\n");

                sb.append("Appointment ID: ")
                        .append(patientAppointment.getAppointmentId())
                        .append("\n");

                sb.append("Recorded: ")
                        .append(diagnosis.getDateRecord())
                        .append("\n");

                sb.append("Condition Start: ")
                        .append(diagnosis.getDateStartCondition())
                        .append("\n");

                sb.append("Condition End: ")
                        .append(diagnosis.getDateEndCondition())
                        .append("\n");

                sb.append("Description: ")
                        .append(diagnosis.getDescription())
                        .append("\n\n");
            }
        }

        if (!foundDiagnosis) {
            sb.append("No diagnosis history found.\n");
        }

        if (appointment.getNote() != null) {

            sb.append("Note\n");
            sb.append("----\n");
            sb.append(appointment.getNote().getContent())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Helper method to retrieve detailed information about the doctor associated with a specific appointment.
     * This method constructs a string containing the doctor's personal information, specialization, department, hospital, appointment price, shift schedule, and any notes associated with the appointment.
     * @param appointment The appointment for which to retrieve doctor details.
     * @return A string containing detailed information about the doctor associated with the specified appointment.
     */
    private String getDoctorDetails(Appointment appointment) {

        StringBuilder sb = new StringBuilder();

        Doctor doctor = appointment.getDoctor();

        sb.append("Doctor Information\n");
        sb.append("-------------------\n");

        sb.append("Doctor ID: ").append(doctor.getId()).append("\n");
        sb.append("Name: ").append(doctor.getName()).append("\n");
        sb.append("Surname: ").append(doctor.getSurname()).append("\n");
        sb.append("Email: ").append(doctor.getEmail()).append("\n");

        if (doctor.getSpecialization() != null) {
            sb.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
        }

        if (doctor.getDepartment() != null) {

            sb.append("Department: ")
                    .append(doctor.getDepartment().getName())
                    .append("\n");

            if (doctor.getDepartment().getHospital() != null) {
                sb.append("Hospital: ")
                        .append(doctor.getDepartment().getHospital().getName())
                        .append("\n");
            }
        }

        if (appointment.getNote() != null) {
            sb.append("\nNote\n");
            sb.append("----\n");
            sb.append(appointment.getNote().getContent()).append("\n");
        }


        return sb.toString();
    }
}
