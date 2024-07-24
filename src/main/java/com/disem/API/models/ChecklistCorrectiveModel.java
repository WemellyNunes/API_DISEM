package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_CHECKLIST_CORRECTIVE")
public class ChecklistCorrectiveModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String treatment;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime modificationDate;

    @OneToOne
    @JoinColumn(name = "programing_id")
    private ProgramingModel programing;
}
