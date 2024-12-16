package com.disem.API.services;

import com.disem.API.models.NegationModel;
import com.disem.API.repositories.NegationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NegationService {

    @Autowired
    NegationRepository negationRepository;

    @Transactional
    public NegationModel save(NegationModel negationModel) {
        return negationRepository.save(negationModel);
    }

    public List<NegationModel> findAll() {
        return negationRepository.findAll();
    }

    public Optional<NegationModel> findById(Long id) {
        return negationRepository.findById(id);
    }

    public Optional<NegationModel> findByOrderServiceId(Long orderServiceId) {
        return negationRepository.findByOrderServiceId_Id(orderServiceId);
    }

    public void delete(NegationModel negationModel) {
        negationRepository.delete(negationModel);
    }
}
