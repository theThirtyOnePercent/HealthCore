package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.InsurancePlan;
import it.unitn.healthcore.domain.Patient;
import it.unitn.healthcore.domain.User;
import it.unitn.healthcore.persistence.InsurancePlanRepository;
import it.unitn.healthcore.persistence.PatientRepository;
import it.unitn.healthcore.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PatientService{
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserService userService;

    @Autowired
    public PatientService(InsurancePlanRepository insurancePlanRepository, UserService userService) {
        this.insurancePlanRepository = insurancePlanRepository;
        this.userService = userService;
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
}
