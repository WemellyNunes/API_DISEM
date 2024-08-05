package com.disem.API.controllers;


import com.disem.API.enums.OrdersServices.ClassEnum;
import com.disem.API.services.OrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderStatisticController {

    @Autowired
    OrderServiceService orderServiceService;

    @GetMapping("/statistics/classification")
    public ResponseEntity<Map<ClassEnum, Integer>> getClassStatistics() {
        Map<ClassEnum, Integer> ordersClassQuantities = orderServiceService.findOrdersByClassForCurrentMonth();
        return new ResponseEntity<>(ordersClassQuantities, HttpStatus.OK);
    }

}
