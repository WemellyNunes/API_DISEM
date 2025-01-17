package com.disem.API.repositories;

import com.disem.API.enums.OrdersServices.*;
import com.disem.API.models.OrderServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderServiceRepository extends JpaRepository<OrderServiceModel, Long> {
    Optional<OrderServiceModel> findByRequisition(Integer requisition);

    List<OrderServiceModel> findByOrigin(OriginEnum origin);

    List<OrderServiceModel> findByStatus(StatusEnum status);

    List<OrderServiceModel> findByRequester(String requester);

    List<OrderServiceModel> findByUnit(String unit);

    List<OrderServiceModel> findBySystem(SystemEnum system);

    List<OrderServiceModel> findByClassification(ClassEnum classification);

    List<OrderServiceModel> findByTypeMaintenance(TypeMaintenanceEnum typeMaintenance);

    Integer countByClassificationAndDateBetween(ClassEnum classification, LocalDate startDate, LocalDate endDate);


    //Estatisticas do dashboard

    Integer countByClassificationAndDateBetweenAndStatusNot(ClassEnum classification, LocalDate startDate, LocalDate endDate, StatusEnum excludedStatus);


    long countByDateBetweenAndStatusNot(LocalDate startDate, LocalDate endDate, StatusEnum excludedStatus);

    long countByTypeMaintenanceAndDateBetweenAndStatusNot(TypeMaintenanceEnum typeMaintenance, LocalDate startDate, LocalDate endDate, StatusEnum excludedStatus);

    long countBySystemAndDateBetweenAndStatusNot(SystemEnum system, LocalDate startDate, LocalDate endDate, StatusEnum excludedStatus);

    long countByOriginAndStatus(OriginEnum origin, StatusEnum status);

    long countByStatusAndDateBetween(StatusEnum status, LocalDate startDate, LocalDate endDate);


    long countOrdersByCampusAndDateBetweenAndStatusNot(CampusEnum campus, LocalDate startDate, LocalDate endDate, StatusEnum excludedStatus);


    long countByOriginAndStatusNotIn(OriginEnum origin, List<StatusEnum> statuses);

    long countByStatusNotInAndDateBetween(List<StatusEnum> statuses, LocalDate startDate, LocalDate endDate);

}

