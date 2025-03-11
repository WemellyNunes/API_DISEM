package com.disem.API.enums.OrdersServices;

public enum RoleEnum {
    CONFIG(0),
    ADMIN(1),
    COLABORADOR_I(2),
    COLABORADOR_II(3),
    USUARIO(4);

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
        throw new IllegalArgumentException("Papel invalido: " + value);
    }
}
