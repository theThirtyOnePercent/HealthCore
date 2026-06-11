package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.Hospital;
import it.unitn.healthcore.persistence.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService (HospitalRepository hospitalRepository){
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> getAllHospital(){
        return this.hospitalRepository.findAll();
    }
}
