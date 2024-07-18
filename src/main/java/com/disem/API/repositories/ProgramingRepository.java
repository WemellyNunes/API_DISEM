package com.disem.API.repositories;

import com.disem.API.models.ProgramingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramingRepository extends JpaRepository<ProgramingModel, UUID> {

}