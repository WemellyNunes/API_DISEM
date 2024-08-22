package com.disem.API.repositories;

import com.disem.API.models.PreventiveEquipamentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PreventiveEquipamentRepository extends JpaRepository<PreventiveEquipamentModel, Long> {
}
