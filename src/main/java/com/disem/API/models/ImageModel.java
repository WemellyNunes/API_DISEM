package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "TB_IMAGE")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String nameFile;

    @NotNull
    private String description;

    @ManyToOne
    @JoinColumn(name = "programing_id")
    private ProgramingModel programing;
}
