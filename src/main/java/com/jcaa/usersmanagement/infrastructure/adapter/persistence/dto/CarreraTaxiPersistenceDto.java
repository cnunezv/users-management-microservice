package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

import java.math.BigDecimal;

public record CarreraTaxiPersistenceDto(
    String id,
    String cliente,
    String taxi,
    BigDecimal kilometros,
    String barrioInicio,
    String barrioLlegada,
    int cantidadPasajeros,
    String taxista,
    BigDecimal precio,
    int duracionMinutos,
    String createdAt,
    String updatedAt) {}
