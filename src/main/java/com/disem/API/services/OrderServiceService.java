package com.disem.API.services;

import com.disem.API.models.OrderServiceModel;
import com.disem.API.repositories.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceService {

    @Autowired
    OrderServiceRepository orderServiceRepository;

    @Transactional
    public OrderServiceModel save(OrderServiceModel orderServiceModel){
        return orderServiceRepository.save(orderServiceModel);
    }

    public Optional<OrderServiceModel> findById(UUID id){
        return orderServiceRepository.findById(id);
    }

    public Page<OrderServiceModel> findAll(Pageable pageable) {
        return orderServiceRepository.findAll(pageable);
    }

    public void delete(OrderServiceModel orderServiceModel){
        orderServiceRepository.delete(orderServiceModel);
    }

}
