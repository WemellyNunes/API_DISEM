package com.disem.API.services;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.repositories.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderStatisticService {

    @Autowired
    OrderServiceRepository orderServiceRepository;

    private LocalDate[] getStartAndOfMonth(Integer year, Integer month) {
        if (year == null || month == null) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return new LocalDate[]{startDate, endDate};
    }

    public Map<ClassEnum, Integer> findOrdersByClassForCurrentMonth(Integer year, Integer month) {
        LocalDate[] dates = getStartAndOfMonth(year, month);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        Map<ClassEnum, Integer> ordersClassQuantities = new HashMap<>();
        for (ClassEnum classEnum : ClassEnum.values()) {
            Integer count = orderServiceRepository.countByClassificationAndDateBetweenAndStatusNot(
                    classEnum, startDate, endDate, StatusEnum.NEGADA
            );
            ordersClassQuantities.put(classEnum, count);
        }
        return ordersClassQuantities;
    }

    public Map<TypeMaintenanceEnum, Double> findOrdersByTypeForCurrentMonth(Integer year, Integer month) {
        LocalDate[] dates = getStartAndOfMonth(year, month);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        long totalOrders = orderServiceRepository.countByDateBetweenAndStatusNot(startDate, endDate, StatusEnum.NEGADA);

        Map<TypeMaintenanceEnum, Double> ordersTypeQuantities = new HashMap<>();
        for (TypeMaintenanceEnum typeMaintenanceEnum : TypeMaintenanceEnum.values()) {
            long count = orderServiceRepository.countByTypeMaintenanceAndDateBetweenAndStatusNot(
                    typeMaintenanceEnum, startDate, endDate, StatusEnum.NEGADA
            );
            double percentage = totalOrders > 0 ? Math.round((double) count / totalOrders * 100) : 0.0;
            ordersTypeQuantities.put(typeMaintenanceEnum, percentage);
        }
        return ordersTypeQuantities;
    }

    public Map<SystemEnum, Double> findOrdersBySystemForYear(Integer year) {
        if (year == null) year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        long totalOrders = orderServiceRepository.countByDateBetweenAndStatusNot(startDate, endDate, StatusEnum.NEGADA);

        Map<SystemEnum, Double> ordersSystemQuantities = new HashMap<>();
        for (SystemEnum systemEnum : SystemEnum.values()) {
            long count = orderServiceRepository.countBySystemAndDateBetweenAndStatusNot(
                    systemEnum, startDate, endDate, StatusEnum.NEGADA
            );
            double percentage = totalOrders > 0 ? Math.round((double) count / totalOrders * 100) : 0.0;
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
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        System.out.println("Start Date: " + startDateTime);
        System.out.println("End Date: " + endDateTime);

        return findOrdersByStatusesForPeriod(startDate, endDate, status1, status2);
    }

    public Map<String, Long> findOrdersByStatusesForToday(StatusEnum status1, StatusEnum status2) {
        LocalDate today = LocalDate.now();
        return findOrdersByStatusesForPeriod(today, today, status1, status2);
    }

    public Map<String, Long> findOrdersByStatusesForCurrentYear(Integer year, StatusEnum status1, StatusEnum status2) {
        if (year == null) year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfYear());

        return findOrdersByStatusesForPeriod(startDate, endDate, status1, status2);
    }

    public Map<String, Long> findOrdersByCampus(Integer year){
        Map<String, Long> ordersCampusQuantities = new HashMap<>();

        if (year == null) year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        for (CampusEnum campusEnum : CampusEnum.values()) {
            long count = orderServiceRepository.countOrdersByCampusAndDateBetweenAndStatusNot(
                    campusEnum, startDate, endDate, StatusEnum.NEGADA
            );
            ordersCampusQuantities.put(campusEnum.name(), count);
        }
        return ordersCampusQuantities;
    }
}
