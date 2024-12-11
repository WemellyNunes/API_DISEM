package com.disem.API.services;

import com.disem.API.models.FinalizeModel;
import com.disem.API.repositories.FinalizeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinalizeService {

    @Autowired
    FinalizeRepository repo;

    @Transactional
    public FinalizeModel save(FinalizeModel model) {
        return repo.save(model);
    }

    public Page<FinalizeModel> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<FinalizeModel> findByProgramingId(long programingId) {
        return repo.findByProgramingId(programingId);
    }

    public Optional<FinalizeModel> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(FinalizeModel model) {
        repo.delete(model);
    }
}
