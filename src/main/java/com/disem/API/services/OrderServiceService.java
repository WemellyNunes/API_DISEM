package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.repositories.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    //consultas especificas

    public Optional<OrderServiceModel> findByRequisition(Integer requisition){
        return orderServiceRepository.findByRequisition(requisition);
    }

    public List<OrderServiceModel> findByOrigin(OriginEnum origin){
        return orderServiceRepository.findByOrigin(origin);
    }

    public List<OrderServiceModel> findByStatus(StatusEnum status){
        return orderServiceRepository.findByStatus(status);
    }

    public List<OrderServiceModel> findByRequester(String requester){
        return orderServiceRepository.findByRequester(requester);
    }

    public List<OrderServiceModel> findByUnit(String unit){
        return orderServiceRepository.findByUnit(unit);
    }

    public List<OrderServiceModel> findBySystem(SystemEnum system){
        return orderServiceRepository.findBySystem(system);
    }

    public List<OrderServiceModel> findByClassification(ClassEnum classification){
        return orderServiceRepository.findByClassification(classification);
    }

    public List<OrderServiceModel> findByTypeMaintenance(TypeMaintenanceEnum type){
        return orderServiceRepository.findByTypeMaintenance(type);
    }

    public List<OrderServiceModel> findByCreationDate(LocalDateTime creationDate){
        return orderServiceRepository.findByCreationDate(creationDate);
    }
}
