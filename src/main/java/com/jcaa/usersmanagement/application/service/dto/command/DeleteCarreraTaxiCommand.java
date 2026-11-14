package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;

/** Comando (CQRS): intención de eliminar una carrera de taxi. */
public record DeleteCarreraTaxiCommand(@NotBlank(message = "id must not be blank") String id) {}
