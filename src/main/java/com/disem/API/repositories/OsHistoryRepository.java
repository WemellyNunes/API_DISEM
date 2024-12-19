package com.disem.API.repositories;

import com.disem.API.models.OsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OsHistoryRepository extends JpaRepository<OsHistory, Long> {
    List<OsHistory> findByOrderServiceIdOrderByPerformedAtDesc(Long orderServiceId);
}
