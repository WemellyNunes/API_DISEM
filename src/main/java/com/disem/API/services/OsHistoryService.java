package com.disem.API.services;

import com.disem.API.models.OsHistory;
import com.disem.API.repositories.OsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OsHistoryService {

    @Autowired
    OsHistoryRepository osHistoryRepository;

    public void saveHistory(Long orderServiceId, String action, String performedBy) {
        OsHistory history = new OsHistory();
        history.setOrderServiceId(orderServiceId);
        history.setAction(action);
        history.setPerformedBy(performedBy);
        history.setPerformedAt(LocalDateTime.now());
        osHistoryRepository.save(history);
    }
}
