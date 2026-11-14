package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiBarrio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCantidadPasajeros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCliente;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiDuracionMinutos;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiKilometros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiPrecio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxi;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxista;
import lombok.experimental.UtilityClass;

/**
 * Traduce los DTOs de aplicación (comandos y queries) a objetos del dominio. Es el punto donde los
 * datos primitivos se convierten en value objects y, por tanto, donde se aplican las reglas de
 * negocio.
 */
@UtilityClass
public class CarreraTaxiApplicationMapper {

  public CarreraTaxiModel fromCreateCommandToModel(final CreateCarreraTaxiCommand command) {
    return CarreraTaxiModel.create(
        new CarreraTaxiId(command.id()),
        new CarreraTaxiCliente(command.cliente()),
        new CarreraTaxiTaxi(command.taxi()),
        new CarreraTaxiKilometros(command.kilometros()),
        new CarreraTaxiBarrio(command.barrioInicio()),
        new CarreraTaxiBarrio(command.barrioLlegada()),
        new CarreraTaxiCantidadPasajeros(command.cantidadPasajeros()),
        new CarreraTaxiTaxista(command.taxista()),
        new CarreraTaxiPrecio(command.precio()),
        new CarreraTaxiDuracionMinutos(command.duracionMinutos()));
  }

  public CarreraTaxiModel fromUpdateCommandToModel(final UpdateCarreraTaxiCommand command) {
    return new CarreraTaxiModel(
        new CarreraTaxiId(command.id()),
        new CarreraTaxiCliente(command.cliente()),
        new CarreraTaxiTaxi(command.taxi()),
        new CarreraTaxiKilometros(command.kilometros()),
        new CarreraTaxiBarrio(command.barrioInicio()),
        new CarreraTaxiBarrio(command.barrioLlegada()),
        new CarreraTaxiCantidadPasajeros(command.cantidadPasajeros()),
        new CarreraTaxiTaxista(command.taxista()),
        new CarreraTaxiPrecio(command.precio()),
        new CarreraTaxiDuracionMinutos(command.duracionMinutos()));
  }

  public CarreraTaxiId fromGetByIdQueryToCarreraTaxiId(final GetCarreraTaxiByIdQuery query) {
    return new CarreraTaxiId(query.id());
  }

  public CarreraTaxiId fromDeleteCommandToCarreraTaxiId(final DeleteCarreraTaxiCommand command) {
    return new CarreraTaxiId(command.id());
  }
}
