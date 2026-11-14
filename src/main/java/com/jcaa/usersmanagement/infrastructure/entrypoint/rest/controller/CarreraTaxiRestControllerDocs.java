package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CarreraTaxiRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contrato OpenAPI / Swagger para {@link CarreraTaxiRestController}.
 *
 * <p>Toda la documentación vive aquí para mantener el controlador enfocado en el despacho.
 * Permisos: consultar requiere cualquier rol; registrar y actualizar, ADMIN o MEMBER; eliminar,
 * solo ADMIN.
 */
@Tag(
    name = "Carreras de taxi",
    description = "Ejercicio 19: registrar, consultar, actualizar, eliminar y listar carreras.")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public interface CarreraTaxiRestControllerDocs {

  @Operation(
      summary = "Registrar carrera",
      description =
          "Registra una carrera de taxi. El id lo genera el servidor. Roles: ADMIN o MEMBER.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Carrera registrada.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarreraTaxiRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos o regla de negocio incumplida (placa, pasajeros, kilómetros, etc.).",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token o no es válido.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permiso.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CarreraTaxiRestResponse create(@Valid @RequestBody CreateCarreraTaxiRestRequest request);

  @Operation(
      summary = "Listar carreras",
      description = "Lista todas las carreras, de la más reciente a la más antigua.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de carreras. Puede ser vacía.",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CarreraTaxiRestResponse.class)))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token o no es válido.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @SuppressWarnings("unused") // Invocado por Spring MVC y leído por springdoc mediante reflexión.
  List<CarreraTaxiRestResponse> getAll();

  @Operation(
      summary = "Consultar carrera por id",
      description =
          "Devuelve una carrera con su precio por kilómetro y velocidad promedio calculados.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Carrera encontrada.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarreraTaxiRestResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token o no es válido.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una carrera con ese id.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CarreraTaxiRestResponse getById(
      @Parameter(description = "Id (UUID) de la carrera.", required = true) @PathVariable
          String id);

  @Operation(
      summary = "Actualizar carrera",
      description = "Reemplaza los datos de una carrera existente. Roles: ADMIN o MEMBER.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Carrera actualizada.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarreraTaxiRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos o regla de negocio incumplida.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token o no es válido.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permiso.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una carrera con ese id.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CarreraTaxiRestResponse update(
      @Parameter(description = "Id (UUID) de la carrera.", required = true) @PathVariable
          String id,
      @Valid @RequestBody UpdateCarreraTaxiRestRequest request);

  @Operation(summary = "Eliminar carrera", description = "Elimina una carrera. Rol: ADMIN.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Carrera eliminada."),
    @ApiResponse(
        responseCode = "401",
        description = "Falta el token o no es válido.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permiso.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe una carrera con ese id.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  void delete(
      @Parameter(description = "Id (UUID) de la carrera.", required = true) @PathVariable
          String id);
}
