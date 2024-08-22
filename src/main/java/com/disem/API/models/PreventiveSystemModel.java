package com.disem.API.models;

import com.disem.API.enums.OrdersServices.SystemEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "TB_PREVENTIVE_SYSTEM")
public class PreventiveSystemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String period;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SystemEnum system;

    @NotBlank
    private String edifice;

    private String observation;

    @NotNull
    private String prevision;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    private LocalDate modificationDate;

    @OneToOne(optional = true)
    @JoinColumn(name = "orderService_id", nullable = true)
    private OrderServiceModel orderService;

}
