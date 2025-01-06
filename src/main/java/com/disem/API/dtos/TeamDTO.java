package com.disem.API.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TeamDTO {

    private String name;

    private String role;

    private Timestamp created_at;
}
