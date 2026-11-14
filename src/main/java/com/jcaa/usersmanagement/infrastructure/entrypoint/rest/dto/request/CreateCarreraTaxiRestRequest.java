package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateCarreraTaxiRestRequest(
    @Schema(example = "María Fernanda Pájaro")
        @NotBlank(message = "cliente must not be blank")
        String cliente,
    @Schema(example = "UAK512", description = "Placa del taxi, formato ABC123")
        @NotBlank(message = "taxi must not be blank")
        String taxi,
    @Schema(example = "6.4") @NotNull(message = "kilometros must not be null")
        BigDecimal kilometros,
    @Schema(example = "El Recreo")
        @NotBlank(message = "barrioInicio must not be blank")
        String barrioInicio,
    @Schema(example = "Centro Histórico")
        @NotBlank(message = "barrioLlegada must not be blank")
        String barrioLlegada,
    @Schema(example = "2", description = "Entre 1 y 4")
        @NotNull(message = "cantidadPasajeros must not be null")
        Integer cantidadPasajeros,
    @Schema(example = "Jorge Luis Marrugo")
        @NotBlank(message = "taxista must not be blank")
        String taxista,
    @Schema(example = "14000", description = "Precio en COP")
        @NotNull(message = "precio must not be null")
        BigDecimal precio,
    @Schema(example = "25") @NotNull(message = "duracionMinutos must not be null")
        Integer duracionMinutos) {}
