package com.disem.API.repositories;

import com.disem.API.models.NegationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NegationRepository extends JpaRepository<NegationModel, Long> {

    Optional<NegationModel> findByOrderServiceId_Id(Long orderServiceId);


}
