package com.disem.API.repositories;

import com.disem.API.models.FinalizeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinalizeRepository extends JpaRepository<FinalizeModel,Long> {

    List<FinalizeModel> findByProgramingId(Long programingId);


}
