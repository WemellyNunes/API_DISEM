package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_PROGRAMING")
public class ProgramingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotBlank
    private String overseer;

    @NotBlank
    private String worker;

    @NotNull
    private Float cost;

    private String observation;

    @NotNull
    private LocalDateTime creatioDate;

    @NotNull
    private LocalDateTime modificationDate;

    @NotBlank
    private String delayedDays;

    @ManyToOne
    @JoinColumn(name = "orderService_id")
    private OrderServiceModel orderServiceModel;
}