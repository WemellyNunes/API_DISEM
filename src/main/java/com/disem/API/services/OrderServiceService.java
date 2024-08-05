package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.repositories.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public Map<ClassEnum, Integer> findOrdersByClassForCurrentMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        Map<ClassEnum, Integer> ordersClassQuantities = new HashMap<>();
        for (ClassEnum classEnum : ClassEnum.values()) {
            ordersClassQuantities.put(classEnum, orderServiceRepository.countByClassificationAndDateBetween(classEnum, startDate, endDate));
        }
        return ordersClassQuantities;
    }

    //public Map<String, Integer> findOrdersByUnit()
}

