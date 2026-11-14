package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiKilometrosException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiKilometros")
class CarreraTaxiKilometrosTest {

  @Test
  @DisplayName("Guarda los kilómetros con dos decimales")
  void shouldUseTwoDecimals() {
    final CarreraTaxiKilometros kilometros = new CarreraTaxiKilometros(new BigDecimal("6.4"));
    assertEquals(new BigDecimal("6.40"), kilometros.value());
    assertEquals("6.40", kilometros.toString());
  }

  @Test
  @DisplayName("Redondea a dos decimales con HALF_UP")
  void shouldRoundHalfUp() {
    assertEquals(
        new BigDecimal("2.35"), new CarreraTaxiKilometros(new BigDecimal("2.345")).value());
  }

  @Test
  @DisplayName("Acepta el máximo de 1000 km")
  void shouldAcceptMaximum() {
    assertEquals(
        new BigDecimal("1000.00"), new CarreraTaxiKilometros(new BigDecimal("1000")).value());
  }

  @Test
  @DisplayName("Rechaza un valor nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiKilometros(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "0.00", "-1", "-0.5"})
  @DisplayName("Rechaza kilómetros en cero o negativos")
  void shouldRejectNotPositive(final String value) {
    final InvalidCarreraTaxiKilometrosException exception =
        assertThrows(
            InvalidCarreraTaxiKilometrosException.class,
            () -> new CarreraTaxiKilometros(new BigDecimal(value)));
    assertTrue(exception.getMessage().contains("mayores que cero"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1000.01", "1500", "99999"})
  @DisplayName("Rechaza más de 1000 km")
  void shouldRejectTooHigh(final String value) {
    final InvalidCarreraTaxiKilometrosException exception =
        assertThrows(
            InvalidCarreraTaxiKilometrosException.class,
            () -> new CarreraTaxiKilometros(new BigDecimal(value)));
    assertTrue(exception.getMessage().contains("1000"));
  }
}
