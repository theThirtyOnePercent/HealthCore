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

@Service
public class AppointmentService {
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(UserService userService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

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
