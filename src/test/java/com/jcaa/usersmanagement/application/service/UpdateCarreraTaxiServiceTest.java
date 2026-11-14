package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCarreraTaxiPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
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
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateCarreraTaxiService")
@ExtendWith(MockitoExtension.class)
class UpdateCarreraTaxiServiceTest {

  @Mock private UpdateCarreraTaxiPort updateCarreraTaxiPort;
  @Mock private GetCarreraTaxiByIdPort getCarreraTaxiByIdPort;

  private UpdateCarreraTaxiService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateCarreraTaxiService(
              updateCarreraTaxiPort, getCarreraTaxiByIdPort, validatorFactory.getValidator());
    }
  }

  private static CarreraTaxiModel sampleCarrera(final String id) {
    return new CarreraTaxiModel(
        new CarreraTaxiId(id),
        new CarreraTaxiCliente("María Fernanda Pájaro"),
        new CarreraTaxiTaxi("UAK512"),
        new CarreraTaxiKilometros(new BigDecimal("6.4")),
        new CarreraTaxiBarrio("El Recreo"),
        new CarreraTaxiBarrio("Centro Histórico"),
        new CarreraTaxiCantidadPasajeros(2),
        new CarreraTaxiTaxista("Jorge Luis Marrugo"),
        new CarreraTaxiPrecio(new BigDecimal("14000")),
        new CarreraTaxiDuracionMinutos(25));
  }

  @Test
  @DisplayName("execute() actualiza la carrera cuando existe")
  void shouldUpdateWhenExists() {
    final CarreraTaxiModel carrera = sampleCarrera("c-001");
    when(getCarreraTaxiByIdPort.getById(new CarreraTaxiId("c-001")))
        .thenReturn(Optional.of(carrera));
    when(updateCarreraTaxiPort.update(carrera)).thenReturn(carrera);

    final CarreraTaxiModel result = service.execute(new UpdateCarreraTaxiCommand(
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

    assertEquals(carrera, result);
    verify(updateCarreraTaxiPort).update(carrera);
  }

  @Test
  @DisplayName("execute() lanza CarreraTaxiNotFoundException si la carrera no existe")
  void shouldThrowWhenNotFound() {
    when(getCarreraTaxiByIdPort.getById(any())).thenReturn(Optional.empty());
    final UpdateCarreraTaxiCommand command = new UpdateCarreraTaxiCommand(
        "no-existe",
        "María Fernanda Pájaro",
        "uak-512",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        2,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25);

    assertThrows(CarreraTaxiNotFoundException.class, () -> service.execute(command));
    verify(updateCarreraTaxiPort, never()).update(any());
  }

  @Test
  @DisplayName("execute() rechaza un comando inválido sin tocar los puertos")
  void shouldRejectInvalidCommand() {
    final UpdateCarreraTaxiCommand command =
        new UpdateCarreraTaxiCommand("", "", "", null, "", "", null, "", null, null);

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(updateCarreraTaxiPort, getCarreraTaxiByIdPort);
  }
}
