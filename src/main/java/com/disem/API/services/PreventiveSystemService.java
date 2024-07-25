package com.disem.API.services;

import com.disem.API.models.PreventiveSystemModel;
import com.disem.API.repositories.PreventiveSystemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PreventiveSystemService {

    @Autowired
    PreventiveSystemRepository repository;

    @Transactional
    public PreventiveSystemModel save(PreventiveSystemModel preventiveSystemModel) {
        return repository.save(preventiveSystemModel);
    }

    public Optional<PreventiveSystemModel> findById(UUID id){
        return repository.findById(id);
    }

    public Page<PreventiveSystemModel> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public void delete(PreventiveSystemModel preventiveSystemModel) {
        repository.delete(preventiveSystemModel);
    }
}
