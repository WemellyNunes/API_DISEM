package com.disem.API.models;

import com.disem.API.enums.OrdersServices.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "TB_ORDER_SERVICE")
public class OrderServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OriginEnum origin;

    @NotNull
    private Integer requisition;

    @NotNull
    @Enumerated(EnumType.STRING)
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
    @Enumerated(EnumType.STRING)
    private TypeMaintenanceEnum typeMaintenance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SystemEnum system;

    @NotBlank
    private String maintenanceUnit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CampusEnum campus;

    @NotBlank
    private String maintenanceIndicators;


    private String observation;

    @Enumerated(EnumType.STRING)
    private ImpactEnum impact;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeTreatmentEnum typeTreatment;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalDateTime modificationDate;

    @NotBlank
    private String openDays;

    @JsonIgnore
    @OneToMany(mappedBy = "orderService")
    private List<ProgramingModel> programings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "orderService")
    private List<DocumentModel> documents = new ArrayList<>();

}
