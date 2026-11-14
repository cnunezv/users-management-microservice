package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

import java.math.BigDecimal;

/** Carrera de taxi expuesta por la API, con los valores que calcula el dominio. */
public record CarreraTaxiRestResponse(
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
    BigDecimal precioPorKilometro,
    BigDecimal velocidadPromedioKmH) {}
