package com.disem.API.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "TB_ORDER_SERVICE")
public class OrderService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String requisition;

}
