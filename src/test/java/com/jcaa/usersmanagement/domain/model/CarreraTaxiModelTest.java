package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CarreraTaxiModel")
class CarreraTaxiModelTest {

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
  @DisplayName("create() conserva todos los atributos")
  void shouldCreateWithAllAttributes() {
    final CarreraTaxiModel carrera = sampleCarrera();

    assertEquals("c-001", carrera.getId().value());
    assertEquals("María Fernanda Pájaro", carrera.getCliente().value());
    assertEquals("UAK512", carrera.getTaxi().value());
    assertEquals(new BigDecimal("6.40"), carrera.getKilometros().value());
    assertEquals("El Recreo", carrera.getBarrioInicio().value());
    assertEquals("Centro Histórico", carrera.getBarrioLlegada().value());
    assertEquals(2, carrera.getCantidadPasajeros().value());
    assertEquals("Jorge Luis Marrugo", carrera.getTaxista().value());
    assertEquals(new BigDecimal("14000.00"), carrera.getPrecio().value());
    assertEquals(25, carrera.getDuracionMinutos().value());
  }

  @Test
  @DisplayName("precioPorKilometro() divide el precio entre los kilómetros")
  void shouldComputePrecioPorKilometro() {
    assertEquals(new BigDecimal("2187.50"), sampleCarrera().precioPorKilometro());
  }

  @Test
  @DisplayName("velocidadPromedioKmH() calcula km/h a partir de los minutos")
  void shouldComputeVelocidadPromedio() {
    assertEquals(new BigDecimal("15.36"), sampleCarrera().velocidadPromedioKmH());
  }

  @Test
  @DisplayName("esDentroDelMismoBarrio() es falso cuando los barrios difieren")
  void shouldDetectDifferentBarrios() {
    assertFalse(sampleCarrera().esDentroDelMismoBarrio());
  }

  @Test
  @DisplayName("esDentroDelMismoBarrio() ignora mayúsculas y minúsculas")
  void shouldDetectSameBarrioIgnoringCase() {
    final CarreraTaxiModel base = sampleCarrera();
    final CarreraTaxiModel carrera =
        new CarreraTaxiModel(
            base.getId(),
            base.getCliente(),
            base.getTaxi(),
            base.getKilometros(),
            new CarreraTaxiBarrio("Manga"),
            new CarreraTaxiBarrio("manga"),
            base.getCantidadPasajeros(),
            base.getTaxista(),
            base.getPrecio(),
            base.getDuracionMinutos());

    assertTrue(carrera.esDentroDelMismoBarrio());
  }

  @Test
  @DisplayName("changePrecio() devuelve una nueva instancia sin modificar la original")
  void shouldChangePrecioImmutably() {
    final CarreraTaxiModel original = sampleCarrera();
    final CarreraTaxiModel cambiada =
        original.changePrecio(new CarreraTaxiPrecio(new BigDecimal("16000")));

    assertEquals(new BigDecimal("16000.00"), cambiada.getPrecio().value());
    assertEquals(new BigDecimal("14000.00"), original.getPrecio().value());
    assertEquals(original.getId(), cambiada.getId());
    assertNotSame(original, cambiada);
  }

  @Test
  @DisplayName("Dos carreras con los mismos valores son iguales")
  void shouldBeEqualByValue() {
    assertEquals(sampleCarrera(), sampleCarrera());
    assertEquals(sampleCarrera().hashCode(), sampleCarrera().hashCode());
  }
}
