package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.business.AdministratorController;
import it.unitn.healthcore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @class AdministratorController
 * @brief AdministratorController is a Rest controller that handles HTTP request related tot administrator 
 * @see AdministratorService
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */

public class AdministratorController {
    private final AdministratorService administratorService;
    @Autowired
    /** @brief Constructor for the AdministratorController class.
     * This constructor is used to inject the AdministratorService dependency into the controller.
     * The @Autowired annotation allows Spring to automatically wire the service when creating an instance of the controller.
     * @param administratorService The service that provides business logic related to administrators.
     */
    public AdministratorController(AdministratorService administratorService){
        this.administratorService = administratorService;
    }
}
