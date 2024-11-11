package com.disem.API.dtos;

import com.disem.API.enums.OrdersServices.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;

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

    private String contact;

    @NotNull
    private String preparationObject;

    @NotNull
    private TypeMaintenanceEnum typeMaintenance;

    @NotNull
    private SystemEnum system;

    @NotBlank
    private String maintenanceUnit;

    @NotNull
    private CampusEnum campus;

    @NotBlank
    private String maintenanceIndicators;

    private String observation;

    @NotNull
    private TypeTreatmentEnum typeTreatment;

    @NotNull
    private StatusEnum status;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalDate modificationDate;

    @NotBlank
    private String openDays;

}
