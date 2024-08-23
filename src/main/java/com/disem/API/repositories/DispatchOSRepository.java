package com.disem.API.repositories;

import com.disem.API.models.DispatchOSModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatchOSRepository extends JpaRepository<DispatchOSModel,Long> {

    List<DispatchOSModel> findByOrderServiceId(Long orderServiceId);

}
