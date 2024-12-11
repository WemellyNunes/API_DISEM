package com.disem.API.enums.OrdersServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CampusEnum {

    MARABA("MARABA"),
    SANTANA_DO_ARAGUAIA("SANTANA DO ARAGUAIA"),
    XINGUARA("XINGUARA"),
    SAO_FELIX_DO_XINGU("SAO FELIX DO XINGU"),
    RONDON("RONDON"),;

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
