package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.PatientService;
import it.unitn.healthcore.domain.InsurancePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
