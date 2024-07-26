package com.disem.API.services;

import com.disem.API.models.PreventiveEquipamentModel;
import com.disem.API.models.PreventiveSystemModel;
import com.disem.API.repositories.PreventiveEquipamentRepository;
import com.disem.API.repositories.PreventiveSystemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PreventiveEquipamentService {

    @Autowired
    PreventiveEquipamentRepository preventiveEquipamentRepository;

    @Transactional
    public PreventiveEquipamentModel save(PreventiveEquipamentModel preventiveEquipamentModel) {
        return preventiveEquipamentRepository.save(preventiveEquipamentModel);
    }

    public Optional<PreventiveEquipamentModel> findById(UUID id) {
        return preventiveEquipamentRepository.findById(id);
    }

    public Page<PreventiveEquipamentModel> findAll(Pageable pageable) {
        return preventiveEquipamentRepository.findAll(pageable);
    }

    public void delete(PreventiveEquipamentModel preventiveEquipamentModel) {
        preventiveEquipamentRepository.delete(preventiveEquipamentModel);
    }
}
