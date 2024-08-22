package com.disem.API.repositories;

import com.disem.API.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    List<ImageModel> findByProgramingId(Long programingId);

}
