package com.disem.API.repositories;

import com.disem.API.models.OrderServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderServiceModel, UUID> {

}
