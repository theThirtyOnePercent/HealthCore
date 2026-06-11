package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.PatientService;
import it.unitn.healthcore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController (PatientService patientService){
        this.patientService = patientService;
    }

    @PostMapping(path = "updateInsurance")
    public InsurancePlan updateInsurancePlan(@RequestParam Integer planId){
        patientService.updateInsurancePlan(planId);
        return patientService.getCurrentInsurancePlan();
    }

    @GetMapping(path = "doctorDescription/{doctorId}")
    public String getDoctorDescription(
            @PathVariable("doctorId") Integer doctorId) {

        return patientService.getDoctorDescription(doctorId);
    }
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

}
