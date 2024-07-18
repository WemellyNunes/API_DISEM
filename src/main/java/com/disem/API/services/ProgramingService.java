package com.disem.API.services;

import com.disem.API.models.ProgramingModel;
import com.disem.API.repositories.ProgramingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProgramingService {

    @Autowired
    ProgramingRepository programingRepository;

    @Transactional
    public ProgramingModel save(ProgramingModel programingModel){
        return programingRepository.save(programingModel);
    }

    public Page<ProgramingModel> findAll(Pageable pageable) {
        return programingRepository.findAll(pageable);
    }

    public Optional<ProgramingModel> findById(UUID id){
        return programingRepository.findById(id);
    }

    public void delete(ProgramingModel programingModel){
        programingRepository.delete(programingModel);
    }
}
