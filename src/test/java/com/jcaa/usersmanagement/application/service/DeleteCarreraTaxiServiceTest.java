package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteCarreraTaxiPort;
import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
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

@DisplayName("DeleteCarreraTaxiService")
@ExtendWith(MockitoExtension.class)
class DeleteCarreraTaxiServiceTest {

  @Mock private DeleteCarreraTaxiPort deleteCarreraTaxiPort;
  @Mock private GetCarreraTaxiByIdPort getCarreraTaxiByIdPort;

  private DeleteCarreraTaxiService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new DeleteCarreraTaxiService(
              deleteCarreraTaxiPort, getCarreraTaxiByIdPort, validatorFactory.getValidator());
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
  @DisplayName("execute() elimina la carrera cuando existe")
  void shouldDeleteWhenExists() {
    when(getCarreraTaxiByIdPort.getById(any())).thenReturn(Optional.of(sampleCarrera("c-001")));

    service.execute(new DeleteCarreraTaxiCommand("c-001"));

    verify(deleteCarreraTaxiPort).delete(new CarreraTaxiId("c-001"));
  }

  @Test
  @DisplayName("execute() lanza CarreraTaxiNotFoundException si no existe")
  void shouldThrowWhenNotFound() {
    when(getCarreraTaxiByIdPort.getById(any())).thenReturn(Optional.empty());
    final DeleteCarreraTaxiCommand command = new DeleteCarreraTaxiCommand("no-existe");

    assertThrows(CarreraTaxiNotFoundException.class, () -> service.execute(command));
    verify(deleteCarreraTaxiPort, never()).delete(any());
  }

  @Test
  @DisplayName("execute() rechaza un id en blanco")
  void shouldRejectBlankId() {
    final DeleteCarreraTaxiCommand command = new DeleteCarreraTaxiCommand(" ");

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(deleteCarreraTaxiPort, getCarreraTaxiByIdPort);
  }
}
