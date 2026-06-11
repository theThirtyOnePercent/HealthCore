package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.DoctorService;
import it.unitn.healthcore.domain.Doctor;
import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.domain.Shift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "doctor")
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

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

    @GetMapping(path = "management/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public Doctor viewDoctorManagementPage (@PathVariable("userId") Integer id){
        return doctorService.findDoctor(id);
    }

    @PostMapping(path = "addShift/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void addShiftTime (@PathVariable("userId") Integer id, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){

        doctorService.addShift(id, startTime, endTime);
    }

    @PutMapping(path = "modifyShift/{shiftId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void modifyShift(@PathVariable Integer shiftId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {

        doctorService.modifyShift(shiftId, startTime, endTime);
    }

    @DeleteMapping(path = "deleteShift/{shiftId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void deleteShift(@PathVariable Integer shiftId) {

        doctorService.deleteShift(shiftId);
    }

    @GetMapping(path = "shifts")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<Shift> viewMyShifts() {

        return doctorService.getCurrentDoctorShifts();
    }

}
