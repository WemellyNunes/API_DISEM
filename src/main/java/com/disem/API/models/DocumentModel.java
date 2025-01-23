package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Entity
@Table(name = "TB_DOCUMENT")
public class DocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nameFile;


    private String description;

    @ManyToOne
    @JoinColumn(name = "orderservice_id")
    private OrderServiceModel orderService;
}
