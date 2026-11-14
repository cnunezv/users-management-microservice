package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

import java.math.BigDecimal;

/** Representa una fila de la tabla carreras_taxi. */
public record CarreraTaxiEntity(
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
