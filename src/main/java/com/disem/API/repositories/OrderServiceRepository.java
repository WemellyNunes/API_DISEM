package com.disem.API.repositories;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderServiceModel, UUID> {
    Optional<OrderServiceModel> findByRequisition(Integer requisition);

    List<OrderServiceModel> findByOrigin(OriginEnum origin);

    List<OrderServiceModel> findByStatus(StatusEnum status);

    List<OrderServiceModel> findByRequester(String requester);

    List<OrderServiceModel> findByUnit(String unit);

    List<OrderServiceModel> findBySystem(SystemEnum system);

    List<OrderServiceModel> findByClassification(ClassEnum classification);

    List<OrderServiceModel> findByTypeMaintenance(TypeMaintenanceEnum typeMaintenance);

   List<OrderServiceModel> findByCreationDate(LocalDateTime creationDate);

}
