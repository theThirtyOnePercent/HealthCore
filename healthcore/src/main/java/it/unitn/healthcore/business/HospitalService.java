package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.persistence.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/** @class HospitalService
 * @brief  HospitalService is a service class that provides business logic related to hospital management. It interacts with the HospitalRepository to perform operations related to retrieving hospital data from the database.
 * @detail This service class contains methods for retrieving the hierarchy of hospitals in the system, which can be used by the HospitalController to provide endpoints for administrators to view hospital information.
 * @detail Also it serves as an intermediary between the controller layer and the persistence layer, ensuring that business rules are applied when accessing hospital data.
 * @see HospitalRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    /** @brief Constructor for the HospitalService class.
     * This constructor is used to inject the HospitalRepository dependency into the service. The @Autowired annotation allows Spring to automatically wire the repository when creating an instance of the service.
     * @param hospitalRepository The repository that provides access to hospital data in the database.
     */
    @Autowired
    public HospitalService (HospitalRepository hospitalRepository){
        this.hospitalRepository = hospitalRepository;
    }
    /** @brief Retrieves a list of all hospitals in the system.
     * This method interacts with the HospitalRepository to retrieve all hospital entities from the database and returns them as a list. It can be used by the HospitalController to provide an endpoint for administrators to view the hierarchy of hospitals in the system.
     * @return A list of Hospital objects representing all hospitals in the system.
     */
    public List<Hospital> getAllHospital(){
        return this.hospitalRepository.findAll();
    }
}
