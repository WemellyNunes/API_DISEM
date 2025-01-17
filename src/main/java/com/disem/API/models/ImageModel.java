package com.disem.API.models;

import com.disem.API.enums.OrdersServices.TypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_IMAGE")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_image_seq")
    @SequenceGenerator(name = "tb_image_seq", sequenceName = "tb_image_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String nameFile;

    private TypeEnum type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "programing_id")
    private ProgramingModel programing;

}
