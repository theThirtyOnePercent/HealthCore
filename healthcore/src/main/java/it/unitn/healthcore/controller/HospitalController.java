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
 * @class RestController for managing hospital-related operations. This controller provides endpoints for administrators to view the hierarchy of hospitals in the system. It interacts with the HospitalService to retrieve hospital data and returns it to the client.
 * @brief  HospitalController is a REST controller that handles HTTP requests related to hospital management. It provides an endpoint for administrators to view the hierarchy of hospitals in the system.
 * @see  HospitalService
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
    /** @brief Constructor for the HospitalController class.
     * This constructor is used to inject the HospitalService dependency into the controller.
     * The @Autowired annotation allows Spring to automatically wire the service when creating an instance of the controller.
     * @param hospitalService The service that provides business logic related to hospitals.
     */
    public HospitalController (HospitalService hospitalService){
        this.hospitalService = hospitalService;
    }
    /** @brief Retrieves a list of all hospitals in the system.
     * This endpoint is accessible only to users with the 'ADMINISTRATOR' role.
     * It returns a list of Hospital objects representing the hierarchy of hospitals in the system.
     * @return A list of Hospital objects.
     */
    @GetMapping(path = "view")
    public List<Hospital> viewHierarchy(){
        return hospitalService.getAllHospital();
    }
}
