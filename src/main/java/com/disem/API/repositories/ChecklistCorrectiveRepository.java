package com.disem.API.repositories;

import com.disem.API.models.ChecklistCorrectiveModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistCorrectiveRepository extends JpaRepository<ChecklistCorrectiveModel, Long> {

    List<ChecklistCorrectiveModel> findByProgramingId(Long programingId);
}
