package com.disem.API.enums.OrdersServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CampusEnum {

    MARABA("Marabá"),
    SANTANA_DO_ARAGUAIA("Santana do Araguaia"),

    XINGUARA("Xinguará"),
    SAO_FELIX_DO_XINGU("São félix do Xingu"),
    RONDON("Rondon"),;

    private final String name;

    CampusEnum(String name) {
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static CampusEnum fromName(String name) {
        for (CampusEnum campusEnum : CampusEnum.values()) {
            if (campusEnum.getName().equalsIgnoreCase(name)) {
                return campusEnum;
            }
        }
        throw new IllegalArgumentException("Campus desconhecido" + name);
    }
}
