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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate datePrograming;

    @NotNull
    private String time;

    @NotBlank
    private String overseer;

    @NotBlank
    private String worker;

    @NotNull
    private Float cost;

    private String observation;

    private LocalDate creationDate;

    private LocalDate modificationDate;

    private String active;

    @ManyToOne
    @JoinColumn(name = "orderService_id", nullable = false)
    private OrderServiceModel orderService;

    @JsonIgnore
    @OneToMany(mappedBy = "programing")
    private List<ImageModel> images;

    @JsonIgnore
    @OneToMany(mappedBy = "programing")
    private List<FinalizeModel> finalizeModels = new ArrayList<>();
}

