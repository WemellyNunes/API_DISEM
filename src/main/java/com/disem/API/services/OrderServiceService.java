package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import com.disem.API.repositories.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.rmi.MarshalledObject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

    //Serviços do dashboard

    public Map<ClassEnum, Integer> findOrdersByClassForCurrentMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        Map<ClassEnum, Integer> ordersClassQuantities = new HashMap<>();
        for (ClassEnum classEnum : ClassEnum.values()) {
            ordersClassQuantities.put(classEnum, orderServiceRepository.countByClassificationAndDateBetween(classEnum, startDate, endDate));
        }
        return ordersClassQuantities;
    }


    public Map<TypeMaintenanceEnum, Double> findOrdersByTypeForCurrentMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        long totalOrders = orderServiceRepository.countByDateBetween(startDate, endDate);

        Map<TypeMaintenanceEnum, Double> ordersTypeQuantities = new HashMap<>();
        for (TypeMaintenanceEnum typeMaintenanceEnum : TypeMaintenanceEnum.values()) {
            long count = orderServiceRepository.countByTypeMaintenanceAndDateBetween(typeMaintenanceEnum, startDate, endDate);
            double percentage = Math.round((double) count / totalOrders * 100);
            ordersTypeQuantities.put(typeMaintenanceEnum, percentage);
        }
        return ordersTypeQuantities;
    }


    public Map<SystemEnum, Double> findOrdersBySystemForCurrentMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        long totalOrders = orderServiceRepository.countByDateBetween(startDate, endDate);

        Map<SystemEnum, Double> ordersSystemQuantities = new HashMap<>();
        for (SystemEnum systemEnum : SystemEnum.values()) {
            long count = orderServiceRepository.countBySystemAndDateBetween(systemEnum, startDate, endDate);
            double percentage = Math.round((double) count / totalOrders * 100);
            ordersSystemQuantities.put(systemEnum, percentage);
        }
        return ordersSystemQuantities;
    }


    public Map<String, Long> findOrdersSipacForStatus(OriginEnum origin, StatusEnum status1, StatusEnum status2){
        long toAttendCount = orderServiceRepository.countByOriginAndStatus(origin, status1);
        long finalizedCount = orderServiceRepository.countByOriginAndStatus(origin, status2);

        Map<String, Long> ordersSipac = new HashMap<>();
        ordersSipac.put("APROVADAS", toAttendCount);
        ordersSipac.put("FINALIZADAS", finalizedCount);
        return ordersSipac;
    }


    public Map<String, Long> findOrdersByStatusesForPeriod(LocalDate startDate, LocalDate endDate, StatusEnum status1, StatusEnum status2){
        long toAttendCount = orderServiceRepository.countByStatusAndDateBetween(status1, startDate, endDate);
        long finalizedCount = orderServiceRepository.countByStatusAndDateBetween(status2, startDate, endDate);

        Map<String, Long> ordersPeriod = new HashMap<>();
        ordersPeriod.put("A_ATENDER", toAttendCount);
        ordersPeriod.put("FINALIZADAS", finalizedCount);
        return ordersPeriod;
    }


    public Map<String, Long> findOrdersByStatusForCurrentMonth(StatusEnum status1, StatusEnum status2) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return findOrdersByStatusesForPeriod(startDate, endDate, status1, status2);
    }


    public Map<String, Long> findOrdersByStatusesForCurrentWeek(StatusEnum status1, StatusEnum status2) {
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        return findOrdersByStatusesForPeriod(startDate, endDate, status1, status2);
    }


    public Map<String, Long> findOrdersByStatusesForToday(StatusEnum status1, StatusEnum status2) {
        LocalDate today = LocalDate.now();
        return findOrdersByStatusesForPeriod(today, today, status1, status2);
    }


    public Map<String, Long> findOrdersByStatusesForCurrentYear(StatusEnum status1, StatusEnum status2) {
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfYear());

        return findOrdersByStatusesForPeriod(startDate, endDate, status1, status2);
    }



    //public Map<String, Integer> findOrdersByUnit()
}

