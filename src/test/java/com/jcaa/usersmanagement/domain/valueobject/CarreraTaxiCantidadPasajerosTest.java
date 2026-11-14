package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiCantidadPasajerosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiCantidadPasajeros")
class CarreraTaxiCantidadPasajerosTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4})
  @DisplayName("Acepta entre 1 y 4 pasajeros")
  void shouldAcceptValidRange(final int value) {
    final CarreraTaxiCantidadPasajeros pasajeros = new CarreraTaxiCantidadPasajeros(value);
    assertEquals(value, pasajeros.value());
    assertEquals(String.valueOf(value), pasajeros.toString());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 5, -1, 9})
  @DisplayName("Rechaza menos de 1 o más de 4 pasajeros")
  void shouldRejectOutOfRange(final int value) {
    final InvalidCarreraTaxiCantidadPasajerosException exception =
        assertThrows(
            InvalidCarreraTaxiCantidadPasajerosException.class,
            () -> new CarreraTaxiCantidadPasajeros(value));
    assertTrue(exception.getMessage().contains("entre 1 y 4"));
  }
}
