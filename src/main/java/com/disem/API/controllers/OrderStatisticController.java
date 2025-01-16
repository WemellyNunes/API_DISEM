package com.disem.API.controllers;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.services.OrderStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "*"
}, allowedHeaders = "*")
public class OrderStatisticController {

    @Autowired
    OrderStatisticService orderStatisticService;

    @GetMapping("/statistics/classification")
    public ResponseEntity<Map<ClassEnum, Integer>> getClassStatistics(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
        Map<ClassEnum, Integer> ordersClassQuantities = orderStatisticService.findOrdersByClassForCurrentMonth(year, month);
        return new ResponseEntity<>(ordersClassQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/typeMaintenance")
    public ResponseEntity<Map<TypeMaintenanceEnum, Double>> getTypeStatistics(@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
        Map<TypeMaintenanceEnum, Double> ordersTypeQuantities = orderStatisticService.findOrdersByTypeForCurrentMonth(year, month);
        return new ResponseEntity<>(ordersTypeQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/system")
    public ResponseEntity<Map<SystemEnum, Double>> getSystemStatistics(@RequestParam(required = false) Integer year) {
        Map<SystemEnum, Double> ordersSystemQuantities = orderStatisticService.findOrdersBySystemForYear(year);
        return new ResponseEntity<>(ordersSystemQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/sipac")
    public ResponseEntity<Map<String, Long>> getSipacOrdersCount(){
        Map<String, Long> ordersSipacQuantities = orderStatisticService.findOrdersSipacForStatus(OriginEnum.SIPAC, StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(ordersSipacQuantities, HttpStatus.OK);
    }

    @GetMapping("/statistics/month")
    public ResponseEntity<Map<String, Long>> getMonthOrdersCount(){
        Map<String, Long> monthOrders = orderStatisticService.findOrdersByStatusForCurrentMonth(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(monthOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/week")
    public ResponseEntity<Map<String, Long>> getWeekOrdersCount() {
        Map<String, Long> weekOrders = orderStatisticService.findOrdersByStatusesForCurrentWeek(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(weekOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/today")
    public ResponseEntity<Map<String, Long>> getTodayOrdersCount(){
        Map<String, Long> todayOrders = orderStatisticService.findOrdersByStatusesForToday(StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(todayOrders, HttpStatus.OK);
    }

    @GetMapping("/statistics/year")
    public ResponseEntity<Map<String,Long>> getYearOrdersCount( @RequestParam(required = false) Integer year) {
        Map<String, Long> yearOrders = orderStatisticService.findOrdersByStatusesForCurrentYear(year, StatusEnum.A_ATENDER, StatusEnum.FINALIZADO);
        return new ResponseEntity<>(yearOrders, HttpStatus.OK);
    }

    @GetMapping("statistics/campus")
    public ResponseEntity<Map<String, Long>> getOrdersByCampus(@RequestParam(required = false) Integer year ) {
        Map<String, Long> campusOrders = orderStatisticService.findOrdersByCampus(year);
        return new ResponseEntity<>(campusOrders, HttpStatus.OK);
    }
}
