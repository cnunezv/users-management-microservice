package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

/** Query (CQRS): consulta de una carrera de taxi por su id. */
public record GetCarreraTaxiByIdQuery(@NotBlank(message = "id must not be blank") String id) {}
