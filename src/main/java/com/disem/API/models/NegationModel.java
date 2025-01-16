package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "TB_NEGATION")
public class NegationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String content;

    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "orderservice_id", referencedColumnName = "id", nullable = false)
    private OrderServiceModel orderService;
}
