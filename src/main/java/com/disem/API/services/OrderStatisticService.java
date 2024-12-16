package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.repositories.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderStatisticService {

    @Autowired
    OrderServiceRepository orderServiceRepository;

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
        List<StatusEnum> excludedStatuses = java.util.Arrays.asList(StatusEnum.NEGADA);

        long toAttendCount = orderServiceRepository.countByOriginAndStatusNotIn(origin, excludedStatuses);
        long finalizedCount = orderServiceRepository.countByOriginAndStatus(origin, StatusEnum.FINALIZADO);

        Map<String, Long> ordersSipac = new HashMap<>();
        ordersSipac.put("A_ATENDER", toAttendCount);
        ordersSipac.put("FINALIZADO", finalizedCount);
        return ordersSipac;
    }


    public Map<String, Long> findOrdersByStatusesForPeriod(LocalDate startDate, LocalDate endDate, StatusEnum status1, StatusEnum status2){
        List<StatusEnum> excludedStatuses = java.util.Arrays.asList(StatusEnum.NEGADA);

        long toAttendCount = orderServiceRepository.countByStatusNotInAndDateBetween(excludedStatuses, startDate, endDate);
        long finalizedCount = orderServiceRepository.countByStatusAndDateBetween(StatusEnum.FINALIZADO, startDate, endDate);

        Map<String, Long> ordersPeriod = new HashMap<>();
        ordersPeriod.put("A_ATENDER", toAttendCount);
        ordersPeriod.put("FINALIZADO", finalizedCount);
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

    public Map<String, Long> findOrdersByCampus(){
        Map<String, Long> ordersCampusQuantities = new HashMap<>();

        for (CampusEnum campusEnum : CampusEnum.values()) {
            long count = orderServiceRepository.countOrdersByCampus(campusEnum);
            ordersCampusQuantities.put(campusEnum.name(), count);
        }
        return ordersCampusQuantities;
    }
}
