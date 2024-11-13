package com.disem.API.enums.OrdersServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum {
    A_ATENDER("A atender"),
    EM_ATENDIMENTO("Em atendimento"),
    RESOLVIDO("Resolvido"),
    FINALIZADO("Finalizado"),
    REABERTO("Reaberto");

    private final String descricao;

    StatusEnum(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static StatusEnum fromDescricao(String descricao) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.descricao.equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + descricao);
    }
}
