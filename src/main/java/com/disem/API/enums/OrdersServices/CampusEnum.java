package com.disem.API.enums.OrdersServices;

public enum CampusEnum {

    MARABA("Marabá"),
    SANTANA_DO_ARAGUAIA("Santana do Araguaia"),
    XINGUARA("Xinguara"),
    SAO_FELIX_DO_XINGU("São Félix do Xingu"),
    RONDON_DO_PARA("Rondon");

    private final String name;

    CampusEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
