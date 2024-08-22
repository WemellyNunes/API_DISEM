package com.disem.API.repositories;

import com.disem.API.models.PreventiveSystemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PreventiveSystemRepository extends JpaRepository<PreventiveSystemModel, Long> {
    Optional<PreventiveSystemModel> findById(Long id);
}
