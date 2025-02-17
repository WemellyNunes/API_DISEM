package com.disem.API.models;

import com.disem.API.enums.OrdersServices.TypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_IMAGE")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nameFile;

    private TypeEnum type;

    private String description;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "programing_id")
    private ProgramingModel programing;

}
