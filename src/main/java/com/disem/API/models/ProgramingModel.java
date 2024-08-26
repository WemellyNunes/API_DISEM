package com.disem.API.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "TB_PROGRAMING")
public class ProgramingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDate datePrograming;

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
    private LocalDate creationDate;

    @NotNull
    private LocalDate modificationDate;

    @NotBlank
    private String delayedDays;

    private String active;

    @ManyToOne
    @JoinColumn(name = "orderService_id", nullable = false)
    private OrderServiceModel orderService;

    @JsonIgnore
    @OneToMany(mappedBy = "programing")
    private List<ImageModel> images;
}
