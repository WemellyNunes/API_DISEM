package com.disem.API.enums.OrdersServices;

public enum TypeMaintenanceEnum {
    MANUTENCAO_CORRETIVA("Manutenção corretiva"),
    MANUTENCAO_PREVENTIVA("Manutenção preventiva"),
    EXTRAMANUTENCAO("Extramanutenção");

    private String legend;

    TypeMaintenanceEnum(String legend){
        this.legend = legend;
    }

    public String getLegend(){
        return this.legend;
    }
}
