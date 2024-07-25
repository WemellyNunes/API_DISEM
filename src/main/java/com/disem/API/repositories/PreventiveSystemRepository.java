package com.disem.API.repositories;

import com.disem.API.models.PreventiveSystemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreventiveSystemRepository extends JpaRepository<PreventiveSystemModel, UUID> {
    Optional<PreventiveSystemModel> findById(UUID id);
}
