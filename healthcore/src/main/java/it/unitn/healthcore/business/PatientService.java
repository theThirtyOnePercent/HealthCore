package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
import it.unitn.healthcore.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService{
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public PatientService(InsurancePlanRepository insurancePlanRepository, UserService userService, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.insurancePlanRepository = insurancePlanRepository;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

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

    private Boolean validatePlan(Integer planId, Integer userId){

        //This is where the system would check with insurance system
        // if the user is actually subscribed with that plan

        return true;
    }

    public InsurancePlan getCurrentInsurancePlan (){
        Patient current_patient =(Patient) userService.getCurrentUser();

        return current_patient.getInsurancePlan();
    }

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

    private boolean overlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private Boolean payment(Patient patient, Doctor doctor) {
        //This is where the payment would be implemented
        return true;
    }


    @Transactional
    public Appointment bookAppointment(Integer doctorId, LocalDateTime start, LocalDateTime end) {

        Patient patient = (Patient) userService.getCurrentUser();

        if (!start.isBefore(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        // Check if it is during doctor shift time

        boolean insideShift = doctor.getShifts().stream()
                .anyMatch(shift -> !start.isBefore(shift.getStartTime()) && !end.isAfter(shift.getEndTime()));

        if (!insideShift) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is not working during the requested time");
        }

        // Check doctor overlap
        for (Appointment a : doctor.getAppointments()) {
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
}
