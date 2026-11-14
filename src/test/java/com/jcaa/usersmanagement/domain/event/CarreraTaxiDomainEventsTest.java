package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

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
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Eventos de dominio de CarreraTaxi")
class CarreraTaxiDomainEventsTest {

  static CarreraTaxiModel sampleCarrera() {
    return CarreraTaxiModel.create(
        new CarreraTaxiId("c-001"),
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
  @DisplayName("CarreraTaxiCreatedDomainEvent expone el nombre y todos los datos de la carrera")
  void shouldBuildCreatedEvent() {
    final CarreraTaxiModel carrera = sampleCarrera();
    final CarreraTaxiCreatedDomainEvent event = new CarreraTaxiCreatedDomainEvent(carrera);

    assertEquals("carrera_taxi.created", event.getEventName());
    assertNotNull(event.getOccurredOn());
    assertSame(carrera, event.getCarrera());

    final Map<String, String> payload = event.payload();
    assertEquals(10, payload.size());
    assertEquals("c-001", payload.get("id"));
    assertEquals("UAK512", payload.get("taxi"));
    assertEquals("6.40", payload.get("kilometros"));
    assertEquals("2", payload.get("cantidadPasajeros"));
    assertEquals("14000.00", payload.get("precio"));
    assertEquals("25", payload.get("duracionMinutos"));
  }

  @Test
  @DisplayName("CarreraTaxiUpdatedDomainEvent expone el nombre y todos los datos de la carrera")
  void shouldBuildUpdatedEvent() {
    final CarreraTaxiUpdatedDomainEvent event = new CarreraTaxiUpdatedDomainEvent(sampleCarrera());

    assertEquals("carrera_taxi.updated", event.getEventName());
    assertEquals(10, event.payload().size());
    assertEquals("Jorge Luis Marrugo", event.payload().get("taxista"));
  }

  @Test
  @DisplayName("CarreraTaxiDeletedDomainEvent expone solo el id")
  void shouldBuildDeletedEvent() {
    final CarreraTaxiDeletedDomainEvent event =
        new CarreraTaxiDeletedDomainEvent(new CarreraTaxiId("c-001"));

    assertEquals("carrera_taxi.deleted", event.getEventName());
    assertEquals(new CarreraTaxiId("c-001"), event.getCarreraId());
    assertEquals(Map.of("id", "c-001"), event.payload());
  }
}
