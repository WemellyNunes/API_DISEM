package com.disem.API.controllers;


import com.disem.API.enums.OrdersServices.*;
import com.disem.API.services.OrderServiceService;
import com.disem.API.services.OrderStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderStatisticController {

    @Autowired
    OrderStatisticService orderStatisticService;

    @GetMapping("/statistics/classification")
    public ResponseEntity<Map<ClassEnum, Integer>> getClassStatistics() {
        Map<ClassEnum, Integer> ordersClassQuantities = orderStatisticService.findOrdersByClassForCurrentMonth();
        return new ResponseEntity<>(ordersClassQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/typeMaintenance")
    public ResponseEntity<Map<TypeMaintenanceEnum, Double>> getTypeStatistics() {
        Map<TypeMaintenanceEnum, Double> ordersTypeQuantities = orderStatisticService.findOrdersByTypeForCurrentMonth();
        return new ResponseEntity<>(ordersTypeQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/system")
    public ResponseEntity<Map<SystemEnum, Double>> getSystemStatistics() {
        Map<SystemEnum, Double> ordersSystemQuantities = orderStatisticService.findOrdersBySystemForCurrentMonth();
        return new ResponseEntity<>(ordersSystemQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/sipacOrders")
    public ResponseEntity<Map<String, Long>> getSipacOrdersCount(){
        Map<String, Long> ordersSipacQuantities = orderStatisticService.findOrdersSipacForStatus(OriginEnum.SIPAC, StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(ordersSipacQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/ordersMonth")
    public ResponseEntity<Map<String, Long>> getMonthOrdersCount(){
        Map<String, Long> monthOrders = orderStatisticService.findOrdersByStatusForCurrentMonth(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(monthOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/weekOrders")
    public ResponseEntity<Map<String, Long>> getWeekOrdersCount() {
        Map<String, Long> weekOrders = orderStatisticService.findOrdersByStatusesForCurrentWeek(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(weekOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/todayOrders")
    public ResponseEntity<Map<String, Long>> getTodayOrdersCount(){
        Map<String, Long> todayOrders = orderStatisticService.findOrdersByStatusesForToday(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(todayOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/yearOrders")
    public ResponseEntity<Map<String,Long>> getYearOrdersCount() {
        Map<String, Long> yearOrders = orderStatisticService.findOrdersByStatusesForCurrentYear(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(yearOrders, HttpStatus.OK);
    }

}
