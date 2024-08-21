package com.disem.API.repositories;

import com.disem.API.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, UUID> {

    List<ImageModel> findByProgramingId(UUID programingId);

}
