package com.disem.API.repositories;

import com.disem.API.models.InstituteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends JpaRepository<InstituteModel, Long> {
}
