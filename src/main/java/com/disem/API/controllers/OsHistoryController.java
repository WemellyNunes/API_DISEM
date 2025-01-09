package com.disem.API.controllers;

import com.disem.API.models.OsHistory;
import com.disem.API.repositories.OsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "http://localhost:5173", "http://app-disem.com.s3-website-sa-east-1.amazonaws.com/"
}, allowedHeaders = "*")
public class OsHistoryController {

    @Autowired
    OsHistoryRepository osHistoryRepository;

    @GetMapping("/{orderServiceId}/history")
    public ResponseEntity<List<OsHistory>> getOrderServiceHistory(@PathVariable("orderServiceId") Long orderServiceId) {
        List<OsHistory> history = osHistoryRepository.findByOrderServiceIdOrderByPerformedAtDesc(orderServiceId);
        return ResponseEntity.ok(history);
    }
}
