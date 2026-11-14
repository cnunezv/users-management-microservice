package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllCarrerasTaxiPort;
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

@DisplayName("GetAllCarrerasTaxiService")
@ExtendWith(MockitoExtension.class)
class GetAllCarrerasTaxiServiceTest {

  @Mock private GetAllCarrerasTaxiPort getAllCarrerasTaxiPort;

  private GetAllCarrerasTaxiService service;

  @BeforeEach
  void setUp() {
    service = new GetAllCarrerasTaxiService(getAllCarrerasTaxiPort);
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
  @DisplayName("execute() devuelve todas las carreras del puerto")
  void shouldReturnAll() {
    final List<CarreraTaxiModel> carreras = List.of(sampleCarrera("c-001"), sampleCarrera("c-002"));
    when(getAllCarrerasTaxiPort.getAll()).thenReturn(carreras);

    assertEquals(carreras, service.execute());
  }

  @Test
  @DisplayName("execute() devuelve una lista vacía cuando no hay carreras")
  void shouldReturnEmptyList() {
    when(getAllCarrerasTaxiPort.getAll()).thenReturn(List.of());

    assertTrue(service.execute().isEmpty());
  }
}
