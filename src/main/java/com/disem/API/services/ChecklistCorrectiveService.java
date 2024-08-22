package com.disem.API.services;

import com.disem.API.models.ChecklistCorrectiveModel;
import com.disem.API.repositories.ChecklistCorrectiveRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChecklistCorrectiveService {

    @Autowired
    ChecklistCorrectiveRepository checklistCorrectiveRepository;

    @Transactional
    public ChecklistCorrectiveModel save(ChecklistCorrectiveModel checklistCorrectiveModel) {
        return checklistCorrectiveRepository.save(checklistCorrectiveModel);
    }

    public Page<ChecklistCorrectiveModel> findAll(Pageable pageable) {
        return checklistCorrectiveRepository.findAll(pageable);
    }

    public Optional<ChecklistCorrectiveModel> findById(Long id) {
        return checklistCorrectiveRepository.findById(id);
    }

    public void delete(ChecklistCorrectiveModel checklistCorrectiveModel) {
        checklistCorrectiveRepository.delete(checklistCorrectiveModel);
    }

}
