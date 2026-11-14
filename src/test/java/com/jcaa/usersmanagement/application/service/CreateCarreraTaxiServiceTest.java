package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.SaveCarreraTaxiPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiCantidadPasajerosException;
import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxiException;
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

@DisplayName("CreateCarreraTaxiService")
@ExtendWith(MockitoExtension.class)
class CreateCarreraTaxiServiceTest {

  @Mock private SaveCarreraTaxiPort saveCarreraTaxiPort;

  private CreateCarreraTaxiService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new CreateCarreraTaxiService(saveCarreraTaxiPort, validatorFactory.getValidator());
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
  @DisplayName("execute() construye el modelo de dominio y lo guarda")
  void shouldSaveCarrera() {
    final CarreraTaxiModel expected = sampleCarrera("c-001");
    when(saveCarreraTaxiPort.save(any())).thenReturn(expected);

    final CarreraTaxiModel result = service.execute(new CreateCarreraTaxiCommand(
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

    final ArgumentCaptor<CarreraTaxiModel> captor = ArgumentCaptor.forClass(CarreraTaxiModel.class);
    verify(saveCarreraTaxiPort).save(captor.capture());
    assertEquals(expected, captor.getValue());
    assertEquals("UAK512", captor.getValue().getTaxi().value());
    assertSame(expected, result);
  }

  @Test
  @DisplayName("execute() rechaza un comando con campos vacíos sin tocar el puerto")
  void shouldRejectInvalidCommand() {
    final CreateCarreraTaxiCommand command =
        new CreateCarreraTaxiCommand(" ", "", "", null, "", "", null, "", null, null);

    final ConstraintViolationException exception =
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    assertEquals(10, exception.getConstraintViolations().size());
    verifyNoInteractions(saveCarreraTaxiPort);
  }

  @Test
  @DisplayName("execute() aplica las reglas de negocio del dominio")
  void shouldApplyDomainRules() {
    final CreateCarreraTaxiCommand badPlaca = new CreateCarreraTaxiCommand(
        "c-001",
        "María Fernanda Pájaro",
        "12AB",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        2,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25);
    final CreateCarreraTaxiCommand tooManyPassengers = new CreateCarreraTaxiCommand(
        "c-001",
        "María Fernanda Pájaro",
        "uak-512",
        new BigDecimal("6.4"),
        "El Recreo",
        "Centro Histórico",
        5,
        "Jorge Luis Marrugo",
        new BigDecimal("14000"),
        25);

    assertThrows(InvalidCarreraTaxiTaxiException.class, () -> service.execute(badPlaca));
    assertThrows(
        InvalidCarreraTaxiCantidadPasajerosException.class,
        () -> service.execute(tooManyPassengers));
    verifyNoInteractions(saveCarreraTaxiPort);
  }
}
