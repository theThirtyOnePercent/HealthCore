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

@RestController
@RequestMapping(path = "patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Autowired
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
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

    @GetMapping(path = "appointment/detail/{appointmentId}")
    public String viewAppointmentDetail(@PathVariable Integer appointmentId){
        return appointmentService.getAppointmentDetails(appointmentId);
    }

}
