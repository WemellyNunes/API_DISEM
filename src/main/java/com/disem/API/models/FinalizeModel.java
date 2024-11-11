package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "TB_FINALIZE")
public class FinalizeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String content;

    @NotNull
    private LocalDate dateContent;

    @ManyToOne
    @JoinColumn(name = "programing_id", nullable = false)
    private ProgramingModel programing;
}
