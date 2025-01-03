package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.repositories.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class OrderServiceService {

    @Autowired
    OrderServiceRepository orderServiceRepository;

    @Transactional
    public OrderServiceModel save(OrderServiceModel orderServiceModel){
        return orderServiceRepository.save(orderServiceModel);
    }

    public Optional<OrderServiceModel> findById(Long id){
        return orderServiceRepository.findById(id);
    }

    public List<OrderServiceModel> findAll() {
        return orderServiceRepository.findAll();
    }


    public void delete(OrderServiceModel orderServiceModel){
        orderServiceRepository.delete(orderServiceModel);
    }


    @Transactional
    public OrderServiceModel updateOpenDays(Long id) {
        OrderServiceModel order = orderServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OS não encontrada"));

        // Calcula os dias em aberto
        order.calculateOpenDays();

        // Salva a OS com o campo atualizado
        return orderServiceRepository.save(order);
    }


    // Consultas específicas

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

}

