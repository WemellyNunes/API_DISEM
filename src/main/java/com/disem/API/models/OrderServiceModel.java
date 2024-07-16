package com.disem.API.models;

import com.disem.API.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "TB_ORDER_SERVICE")
public class OrderServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private OriginEnum origin;
    @NotBlank
    private String requisition;
    @NotNull
    private ClassEnum classification;
    @NotBlank
    private String unit;
    @NotBlank
    private String requester;
    @NotBlank
    private String contact;
    @NotBlank
    private String preparationObject;
    @NotNull
    private TypeMaintenanceEnum typeMaintenance;
    @NotNull
    private SystemEnum system;
    @NotBlank
    private String maintenanceUnit;
    @NotBlank
    private String maintenanceIndicators;
    @NotBlank
    private String observation;
    @NotNull
    private TypeTreatmentEnum typeTreatment;
    @NotNull
    private StatusEnum status;
    @NotNull
    private LocalDateTime creationDate;
    @NotNull
    private LocalDateTime modificationDate;
    @NotBlank
    private String openDays;

}
