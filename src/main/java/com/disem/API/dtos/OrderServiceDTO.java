package com.disem.API.dtos;

import com.disem.API.enums.OrderServiceEnum.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderServiceDTO {
    @NotNull
    private OriginEnum origin;

    @NotNull
    private Integer requisition;

    @NotNull
    private ClassEnum classification;

    @NotBlank
    private String unit;

    @NotBlank
    private String requester;

    @NotBlank
    private String contact;

    @NotNull
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
