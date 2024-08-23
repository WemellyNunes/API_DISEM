package com.disem.API.services;

import com.disem.API.models.DispatchOSModel;
import com.disem.API.repositories.DispatchOSRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DispatchOSService {

    @Autowired
    DispatchOSRepository repo;

    @Transactional
    public DispatchOSModel save(DispatchOSModel model) {
        return repo.save(model);
    }

    public Page<DispatchOSModel> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Optional<DispatchOSModel> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(DispatchOSModel model) {
        repo.delete(model);
    }
}
