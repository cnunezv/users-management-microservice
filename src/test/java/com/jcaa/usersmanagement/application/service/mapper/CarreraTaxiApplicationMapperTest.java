package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CarreraTaxiApplicationMapper")
class CarreraTaxiApplicationMapperTest {

  @Test
  @DisplayName("fromCreateCommandToModel() convierte cada campo en su value object")
  void shouldMapCreateCommand() {
    final CarreraTaxiModel carrera =
        CarreraTaxiApplicationMapper.fromCreateCommandToModel(new CreateCarreraTaxiCommand(
        "c-001",
        "María Fernanda Pájaro",
        "uak-512",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        2,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25));

    assertEquals("c-001", carrera.getId().value());
    assertEquals("UAK512", carrera.getTaxi().value());
    assertEquals(new BigDecimal("6.40"), carrera.getKilometros().value());
    assertEquals(2, carrera.getCantidadPasajeros().value());
    assertEquals(new BigDecimal("14000.00"), carrera.getPrecio().value());
    assertEquals(25, carrera.getDuracionMinutos().value());
  }

  @Test
  @DisplayName("fromUpdateCommandToModel() produce el mismo modelo que el comando de creación")
  void shouldMapUpdateCommand() {
    assertEquals(
        CarreraTaxiApplicationMapper.fromCreateCommandToModel(new CreateCarreraTaxiCommand(
        "c-001",
        "María Fernanda Pájaro",
        "uak-512",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        2,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25)),
        CarreraTaxiApplicationMapper.fromUpdateCommandToModel(new UpdateCarreraTaxiCommand(
        "c-001",
        "María Fernanda Pájaro",
        "uak-512",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        2,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25)));
  }

  @Test
  @DisplayName("Las queries y el comando de borrado se convierten en CarreraTaxiId")
  void shouldMapIds() {
    assertEquals(
        new CarreraTaxiId("c-001"),
        CarreraTaxiApplicationMapper.fromGetByIdQueryToCarreraTaxiId(
            new GetCarreraTaxiByIdQuery("c-001")));
    assertEquals(
        new CarreraTaxiId("c-002"),
        CarreraTaxiApplicationMapper.fromDeleteCommandToCarreraTaxiId(
            new DeleteCarreraTaxiCommand(" c-002 ")));
  }
}
