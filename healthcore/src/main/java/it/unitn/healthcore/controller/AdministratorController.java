package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.business.AdministratorService;
import it.unitn.healthcore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class AdministratorController {
    private final AdministratorService administratorService;
    @Autowired
    public AdministratorController(AdministratorService administratorService){
        this.administratorService = administratorService;
    }
}
