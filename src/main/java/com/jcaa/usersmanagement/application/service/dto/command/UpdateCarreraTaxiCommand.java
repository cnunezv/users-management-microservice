package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Comando (CQRS): intención de modificar una carrera de taxi existente.
 *
 * <p>Aquí solo se valida la forma de los datos (presencia). Las reglas de negocio viven en los
 * value objects del dominio.
 */
public record UpdateCarreraTaxiCommand(
    @NotBlank(message = "id must not be blank") String id,
    @NotBlank(message = "cliente must not be blank") String cliente,
    @NotBlank(message = "taxi must not be blank") String taxi,
    @NotNull(message = "kilometros must not be null") BigDecimal kilometros,
    @NotBlank(message = "barrioInicio must not be blank") String barrioInicio,
    @NotBlank(message = "barrioLlegada must not be blank") String barrioLlegada,
    @NotNull(message = "cantidadPasajeros must not be null") Integer cantidadPasajeros,
    @NotBlank(message = "taxista must not be blank") String taxista,
    @NotNull(message = "precio must not be null") BigDecimal precio,
    @NotNull(message = "duracionMinutos must not be null") Integer duracionMinutos) {}
