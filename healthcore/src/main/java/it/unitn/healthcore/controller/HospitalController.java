package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.HospitalService;
import it.unitn.healthcore.domain.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @class 
 * @brief  
 * @see 
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */

@RestController
@RequestMapping(path = "hierarchy")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class HospitalController {
    private final HospitalService hospitalService;
    @Autowired
    public HospitalController (HospitalService hospitalService){
        this.hospitalService = hospitalService;
    }

    @GetMapping(path = "view")
    public List<Hospital> viewHierarchy(){
        return hospitalService.getAllHospital();
    }
}
