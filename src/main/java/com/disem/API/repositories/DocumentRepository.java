package com.disem.API.repositories;

import com.disem.API.models.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {
    Optional<DocumentModel> findByOrderServiceId(Long orderServiceId);
}
