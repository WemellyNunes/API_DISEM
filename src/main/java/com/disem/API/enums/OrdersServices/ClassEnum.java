package com.disem.API.enums.OrdersServices;

public enum ClassEnum {
    CLASSE_A("Classe A"),
    CLASSE_B("Classe B"),
    CLASSE_C("Classe C");

    private final String legend;

    ClassEnum(String legend) {
        this.legend = legend;
    }

    public String getLegend() {
        return legend;
    }

}
