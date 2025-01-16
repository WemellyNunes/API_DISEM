package com.disem.API.services;

import com.disem.API.models.InstituteModel;
import com.disem.API.repositories.InstituteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstituteService {

    @Autowired
    InstituteRepository instituteRepository;

    @Transactional
    public InstituteModel save(InstituteModel instituteModel) {
        return instituteRepository.save(instituteModel);
    }

    public List<InstituteModel> findAll() {
        return instituteRepository.findAll();
    }

    public Optional<InstituteModel> findById(Long id) {
        return instituteRepository.findById(id);
    }

    public void delete(InstituteModel instituteModel) {
        instituteRepository.delete(instituteModel);
    }

    @Transactional
    public List<InstituteModel> saveAll(List<InstituteModel> instituteModels) {
        return instituteRepository.saveAll(instituteModels);
    }
}
