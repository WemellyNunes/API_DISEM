package com.disem.API.enums.OrdersServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    ADMIN(0),
    COLABORADOR_I(1),
    COLABORADOR_II(2),
    USUARIO(3);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleEnum fromValue(int value) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
