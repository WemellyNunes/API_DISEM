package com.disem.API.services;

import com.disem.API.models.UnitModel;
import com.disem.API.repositories.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;

    @Transactional
    public UnitModel save(UnitModel unitModel) {
        return unitRepository.save(unitModel);
    }

    public List<UnitModel> findAll() {
        return unitRepository.findAll();
    }

    public Optional<UnitModel> findById(Long id) {
        return unitRepository.findById(id);
    }

    public void delete(UnitModel unitModel) {
        unitRepository.delete(unitModel);
    }

    @Transactional
    public List<UnitModel> saveAll(List<UnitModel> unitModels) {
        return unitRepository.saveAll(unitModels);
    }
}
