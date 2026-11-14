package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CarreraTaxiRestResponse;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarreraTaxiRestMapper {

  /** El id de la carrera lo asigna el servidor: un UUID v4 por cada registro. */
  public CreateCarreraTaxiCommand toCreateCommand(final CreateCarreraTaxiRestRequest request) {
    return new CreateCarreraTaxiCommand(
        UUID.randomUUID().toString(),
        request.cliente(),
        request.taxi(),
        request.kilometros(),
        request.barrioInicio(),
        request.barrioLlegada(),
        request.cantidadPasajeros(),
        request.taxista(),
        request.precio(),
        request.duracionMinutos());
  }

  public UpdateCarreraTaxiCommand toUpdateCommand(
      final String id, final UpdateCarreraTaxiRestRequest request) {
    return new UpdateCarreraTaxiCommand(
        id,
        request.cliente(),
        request.taxi(),
        request.kilometros(),
        request.barrioInicio(),
        request.barrioLlegada(),
        request.cantidadPasajeros(),
        request.taxista(),
        request.precio(),
        request.duracionMinutos());
  }

  public GetCarreraTaxiByIdQuery toGetByIdQuery(final String id) {
    return new GetCarreraTaxiByIdQuery(id);
  }

  public DeleteCarreraTaxiCommand toDeleteCommand(final String id) {
    return new DeleteCarreraTaxiCommand(id);
  }

  public CarreraTaxiRestResponse toResponse(final CarreraTaxiModel carrera) {
    return new CarreraTaxiRestResponse(
        carrera.getId().value(),
        carrera.getCliente().value(),
        carrera.getTaxi().value(),
        carrera.getKilometros().value(),
        carrera.getBarrioInicio().value(),
        carrera.getBarrioLlegada().value(),
        carrera.getCantidadPasajeros().value(),
        carrera.getTaxista().value(),
        carrera.getPrecio().value(),
        carrera.getDuracionMinutos().value(),
        carrera.precioPorKilometro(),
        carrera.velocidadPromedioKmH());
  }

  public List<CarreraTaxiRestResponse> toResponseList(final List<CarreraTaxiModel> carreras) {
    return carreras.stream().map(CarreraTaxiRestMapper::toResponse).toList();
  }
}
