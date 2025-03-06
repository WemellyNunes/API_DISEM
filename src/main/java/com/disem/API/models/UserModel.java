package com.disem.API.models;

import com.disem.API.enums.OrdersServices.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "TB_USER")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private Long idUsuario;

    @NotBlank
    private String nome;

    private String email;

    private RoleEnum papel;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public void setPapel(int value) {
        this.papel = RoleEnum.fromValue(value);
    }

    public int getPapel() {
        return papel.getValue();
    }

}
