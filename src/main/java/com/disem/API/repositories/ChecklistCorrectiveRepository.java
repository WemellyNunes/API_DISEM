package com.disem.API.repositories;

import com.disem.API.models.ChecklistCorrectiveModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistCorrectiveRepository extends JpaRepository<ChecklistCorrectiveModel, UUID> {

    List<ChecklistCorrectiveModel> findByProgramingId(UUID programingId);
}
