package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
import it.unitn.healthcore.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

/** @class PatientService
 * @brief  PatientService is a service class that provides business logic related to patient management. It interacts with the InsurancePlanRepository,
 *  UserService, DoctorRepository, and AppointmentRepository to perform operations related to patient insurance plans, doctor information retrieval, and appointment booking.
 * @detail This service class contains methods for updating a patient's insurance plan, retrieving the current insurance plan, getting detailed information about a doctor, 
 * and booking appointments with doctors. It serves as an intermediary between the controller layer and the persistence layer, ensuring that business rules are applied when 
 * accessing patient-related data and performing operations.
 * @see InsurancePlanRepository
 * @see UserService
 * @see DoctorRepository
 * @see AppointmentRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-12
 */
@Service
public class PatientService{
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    /** @brief Constructor for the PatientService class.
     * This constructor is used to inject the dependencies of the PatientService class, including the InsurancePlan Repository, UserService, DoctorRepository, and AppointmentRepository. The @Autowired annotation allows Spring to automatically wire these dependencies when creating an instance of the PatientService.
     * @param insurancePlanRepository The repository that provides access to insurance plan data in the database.
     * @param userService The service that provides business logic related to user management and authentication.
     * @param doctorRepository The repository that provides access to doctor data in the database.
     * @param appointmentRepository The repository that provides        access to appointment data in the database.
     */ 
    @Autowired
    public PatientService(InsurancePlanRepository insurancePlanRepository, UserService userService, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.insurancePlanRepository = insurancePlanRepository;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }
    /** @brief Updates the insurance plan for the currently authenticated patient.
     * This method takes an insurance plan ID as input and updates the insurance plan for the currently authenticated patient. It first checks if the specified insurance plan exists in the database.
     *  If it does not exist, it throws a ResponseStatusException with a 404 NOT FOUND status. 
     * If the insurance plan exists, it retrieves the current patient from the UserService and validates whether the patient can subscribe to the specified insurance plan. If the validation is successful, 
     * it updates the patient's insurance plan with the new plan.
     * @param planId The ID of the insurance plan to update for the current patient.
     * @throws ResponseStatusException if the specified insurance plan does not exist or if there is an issue with updating the patient's insurance plan.
     */
    @Transactional
    public void updateInsurancePlan (Integer planId){
        Optional<InsurancePlan> optionalPlan = insurancePlanRepository.findById(planId);

        if (optionalPlan.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "insurance plan selected does not exist");
        }

        Patient current_patient =(Patient) userService.getCurrentUser();
        if (validatePlan(planId, current_patient.getId())) {
            current_patient.setInsurancePlan(optionalPlan.get());
        }

    }
    /** @brief Validates whether a patient can subscribe to a specified insurance plan.
     * This method is a placeholder for the actual validation logic that would check with an external insurance system to verify if the patient is subscribed to the specified insurance plan. 
     * In this implementation, it simply returns true, indicating that the validation is successful. In a real-world application, this method would contain logic to interact with an insurance system and perform necessary checks.
     * @param planId The ID of the insurance plan to validate.
     * @param userId The ID of the patient for whom the validation is being performed.
     * @return A boolean value indicating whether the validation was successful (true) or not (false).
     */
    private Boolean validatePlan(Integer planId, Integer userId){

        //This is where the system would check with insurance system
        // if the user is actually subscribed with that plan

        return true;
    }
    /** @brief Retrieves the current insurance plan for the authenticated patient.
     * This method retrieves the currently authenticated patient from the UserService and returns their associated insurance plan. If the patient does not have an insurance plan, it may return null or an appropriate response based on the application's requirements.
     * @return The InsurancePlan object representing the current insurance plan of the authenticated patient, or null if the patient does not have an insurance plan.
     * @throws ResponseStatusException if there is an issue retrieving the current patient's information or if the user is not authenticated as a patient.
     */
    public InsurancePlan getCurrentInsurancePlan (){
        Patient current_patient =(Patient) userService.getCurrentUser();

        return current_patient.getInsurancePlan();
    }
    /** @brief Retrieves detailed information about a doctor based on their ID.
     * This method takes a doctor ID as input and retrieves the corresponding Doctor entity from the DoctorRepository. If the doctor is not found, it throws a ResponseStatusException with a 404 NOT FOUND status. 
     * If the doctor is found, it constructs a string containing detailed information about the doctor, including their name, surname, email, specialization, department, hospital, appointment price, and shift schedule. 
     * The method returns this string as the output.
     * @param doctorId The ID of the doctor for whom to retrieve detailed information.
     * @return A string containing detailed information about the specified doctor.
     * @throws ResponseStatusException if the doctor with the specified ID is not found in the database.
     */
    public String getDoctorDescription(Integer doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Doctor not found"));

        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(doctor.getName()).append("\n");
        sb.append("Surname: ").append(doctor.getSurname()).append("\n");
        sb.append("Email: ").append(doctor.getEmail()).append("\n");
        sb.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
        sb.append("Department: ")
                .append(doctor.getDepartment().getName())
                .append("\n");
        sb.append("Hospital: ")
                .append(doctor.getDepartment().getHospital().getName())
                .append("\n");
        sb.append("Appointment Price: ")
                .append(doctor.getAppointmentPrice())
                .append("\n\n");

        sb.append("Shifts:\n");

        for (Shift shift : doctor.getShifts()) {
            sb.append("- ")
                    .append(shift.getStartTime())
                    .append(" -> ")
                    .append(shift.getEndTime())
                    .append("\n");
        }

        return sb.toString();
    }
    /** @brief Helper method to check if two time intervals overlap.
     * This method takes four LocalDateTime parameters representing the start and end times of two intervals. It checks if the first interval (start1 to end1) overlaps with the second interval (start2 to end2) by comparing their start and end times. 
     * The method returns true if the intervals overlap and false otherwise.
     * @param start1 The start time of the first interval.
     * @param end1 The end time of the first interval.
     * @param start2 The start time of the second interval.
     * @param end2 The end time of the second interval.
     * @return A boolean value indicating whether the two intervals overlap (true) or not (false).
     */
    private boolean overlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }   

    /** @brief Helper method to process payment for an appointment.
     * This method is a placeholder for the actual payment processing logic that would interact with a payment gateway or system to handle payments for appointments. In this implementation, it simply returns true, indicating that the payment was successful. In a real-world application, this method would contain logic to process payments and return the appropriate result based on the outcome of the payment transaction.
     * @param patient The patient for whom the payment is being processed.
     * @param doctor The doctor for whom the payment is being processed.
     * @return A boolean value indicating whether the payment was successful (true) or not (false).
     */
    private Boolean payment(Patient patient, Doctor doctor) {
        //This is where the payment would be implemented
        return true;
    }

    /** @brief Books an appointment with a specified doctor for the currently authenticated patient.
     * This method takes a doctor ID, start time, and end time as input to book an appointment. It performs several checks to ensure that the appointment can be booked, including validating the time intervals, checking the doctor's availability during their shifts, verifying that there are available beds in the department, and confirming insurance coverage or processing payment if necessary. If all checks pass, it creates a new Appointment entity and saves it to the database.
     * @param doctorId The ID of the doctor with whom to book the appointment.
     * @param start The start date and time of the appointment.
     * @param end The end date and time of the appointment.
     * @return The Appointment object representing the booked appointment.
     * @throws ResponseStatusException if any of the checks fail (e.g., invalid time intervals, doctor not available, no beds available, payment failure) or if there is an issue with booking the appointment.
     */
    @Transactional
    public Appointment bookAppointment(Integer doctorId, LocalDateTime start, LocalDateTime end) {
        /**
         * Steps to book an appointment:
         * 1. Validate that the start time is before the end time.
         * 2. Check if the doctor exists.
         * 3. Check if the requested time is during the doctor's shift.
         * 4. Check for overlapping appointments for the doctor.
         * 5. Check bed availability in the department.
         * 6. Check insurance coverage or process payment if necessary.
         * 7. Create and save the appointment.
         */
        Patient patient = (Patient) userService.getCurrentUser();
        // Check doctor existence
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        validateAppointment(patient, doctor, start, end, null);

        // Check insurance
        Hospital hospital = doctor.getDepartment().getHospital();

        boolean covered = patient.getInsurancePlan() != null
                && patient.getInsurancePlan().isCoveredAtHospital(hospital);

        boolean canProceed = false;

        if (!covered) {
            canProceed = payment(patient, doctor);
        }
        else{
            canProceed = true;
        }

        // Create appointment
        if (!canProceed) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Payment was not successful");
        }

        Appointment appointment = new Appointment(patient, doctor, start, end);
        appointmentRepository.save(appointment);

        return appointment;

    }

    /** @brief Modifies an existing appointment for the currently authenticated patient.
     * This method takes an appointment ID, new start time, and new end time as input to modify an existing appointment. It first retrieves the appointment based on the provided ID and checks if it belongs to the currently authenticated patient.
     *  It also checks if the appointment has not already started. If these checks pass, it validates the new appointment details (e.g., time intervals, doctor's availability, bed availability) and updates the appointment with the new start and end times before saving it to the database.
     * @param appointmentId The ID of the appointment to be modified.
     * @param start The new start date and time of the appointment.
     * @param end The new end date and time of the appointment.
     * @throws ResponseStatusException if any of the checks fail (e.g., appointment not found, not assigned to patient, appointment already started, invalid time intervals, doctor not available, no beds available) or if there is an issue with modifying the appointment.
     */
    public void modifyAppointment(Integer appointmentId,LocalDateTime startTime, LocalDateTime endTime){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        Patient patient = (Patient) userService.getCurrentUser();


        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You are not assigned to this appointment");
        }

        //Check if the appointment hasn't already started
        if (!appointment.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot modify an appointment that has already started");
        }

        validateAppointment(patient, appointment.getDoctor(), startTime, endTime, appointmentId);

        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        appointmentRepository.save(appointment);
    }
    /** @brief Validates the details of an appointment to ensure it can be booked or modified.
     * This method performs several checks to validate the appointment details, including verifying that the start time is before the end time, checking if the requested time is during the doctor's shift, ensuring there are no overlapping appointments for the doctor, confirming bed availability in the department, and checking equipment availability. If any of these checks fail, it throws a ResponseStatusException with an appropriate error message.
     * @param patient The patient for whom the appointment is being validated.
     * @param doctor The doctor with whom the appointment is being validated.
     * @param start The proposed start date and time of the appointment.
     * @param end The proposed end date and time of the appointment.
     * @param excludedAppointmentId An optional parameter representing an appointment ID to exclude from overlap checks (used when modifying an existing appointment).
     * @throws ResponseStatusException if any of the validation checks fail (e.g., invalid time intervals, doctor not available, no beds available, not enough equipment available).
     */
    private void validateAppointment(
            Patient patient,
            Doctor doctor,
            LocalDateTime start,
            LocalDateTime end,
            Integer excludedAppointmentId) {

        if (!start.isBefore(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }

        boolean insideShift = doctor.getShifts().stream()
                .anyMatch(shift -> !start.isBefore(shift.getStartTime()) && !end.isAfter(shift.getEndTime()));

        if (!insideShift) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is not working during the requested time");
        }

        // Check doctor overlap
        for (Appointment a : doctor.getAppointments()) {
            if (a.getAppointmentId().equals(excludedAppointmentId)) {
                continue;
            }
            if (overlap(start, end, a.getStartTime(), a.getEndTime())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor already has an appointment at this time");
            }
        }


        // Check bed availability (department level)
        Department department = doctor.getDepartment();

        long overlappingAppointments = doctor.getAppointments().stream()
                .filter(a -> overlap(start, end, a.getStartTime(), a.getEndTime()))
                .count();

        if (overlappingAppointments >= department.getBeds()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No beds available in department at this time");
        }

        // Check equipment availability

        checkEquipmentAvailability(doctor, start, end, excludedAppointmentId);
    }

    /** @brief Checks the availability of equipment required by a doctor during a specified time interval.
     * This method checks if the equipment required by a doctor is available during the specified start and end times. It retrieves overlapping appointments for the doctor's department and counts the quantity of each equipment in use. 
     * It then compares the total required quantity of each equipment (including the new appointment) with the available quantity in the department. If any equipment exceeds its available quantity, it throws a ResponseStatusException with an appropriate error message.
     * @param doctor The doctor for whom to check equipment availability.
     * @param start The proposed start date and time of the appointment.
     * @param end The proposed end date and time of the appointment.
     * @param excludedAppointmentId An optional parameter representing an appointment ID to exclude from overlap checks (used when modifying an existing appointment).
     * @throws ResponseStatusException if any required equipment exceeds its available quantity during the specified time interval.
     */
    private void checkEquipmentAvailability(Doctor doctor, LocalDateTime start, LocalDateTime end, Integer excludedAppointmentId){

        Department department = doctor.getDepartment();

        //Find overlapping appointments

        List<Appointment> overlappingAppointments =
                appointmentRepository.findAll().stream()
                        .filter(a -> !a.getAppointmentId().equals(excludedAppointmentId))
                        .filter(a -> a.getDoctor().getDepartment().getDepartmentId()
                                        .equals(department.getDepartmentId()))
                        .filter(a -> overlap(start, end, a.getStartTime(), a.getEndTime()))
                        .toList();

        //Check equipments in use

        Map<Integer, Integer> equipmentInUse = new HashMap<>();
        for (Appointment appointment : overlappingAppointments) {

            Doctor otherDoctor = appointment.getDoctor();

            for (EquipmentDoctor equipmentDoctor : otherDoctor.getEquipments()) {

                Integer equipmentId = equipmentDoctor.getEquipment().getEquipmentId();

                equipmentInUse.merge(equipmentId, equipmentDoctor.getQuantity(), Integer::sum);
            }
        }

        //Check if new doctor won't cause the equipment maximum to be reached

        for (EquipmentDoctor required : doctor.getEquipments()) {

            Equipment equipment = required.getEquipment();

            int currentlyUsed = equipmentInUse.getOrDefault(equipment.getEquipmentId(), 0);
            int totalRequired = currentlyUsed + required.getQuantity();

            if (totalRequired > equipment.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough " + equipment.getEquipmentType() + " available");
            }
        }
    }
    /** @brief Cancels an existing appointment for the currently authenticated patient.
     * This method takes an appointment ID as input and cancels the corresponding appointment. It first retrieves the appointment based on the provided ID and checks if it belongs to the currently authenticated patient. 
     * If the appointment does not belong to the patient, it throws a ResponseStatusException with a 403 FORBIDDEN status. 
     * If the appointment is valid for cancellation, it checks if the cancellation is happening more than 24 hours before the appointment start time. If so, it processes a refund for the patient. Finally, it deletes the appointment from the database.
     * @param appointmentId The ID of the appointment to be canceled.
     * @throws ResponseStatusException if any of the checks fail (e.g., appointment not found, not assigned to patient) or if there is an issue with canceling the appointment.
     */
    public void cancelAppointment (Integer appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        Patient patient = (Patient) userService.getCurrentUser();

        // Check if the appointment belongs to the patient
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this appointment");
        }


        //Check if operation is happening within 24h. It's not, they receive a refund

        if (appointment.getStartTime().isAfter(LocalDateTime.now().plusHours(24))) {
            refundPayment(patient);
        }

        //Delete

        appointmentRepository.deleteById(appointmentId);
    }
    /** @brief Helper method to process a refund for a patient.
     * This method is a placeholder for the actual refund processing logic that would interact with a payment gateway or system to handle refunds for patients. In this implementation, 
     * it does not perform any actual refund processing. In a real-world application, this method would contain logic to process refunds and return the appropriate result based on the outcome of the refund transaction.
     * @param patient The patient for whom the refund is being processed.
     */
    private void refundPayment(Patient patient){
        //This is where the system would contact the payment system to perform the refund
    }

}
