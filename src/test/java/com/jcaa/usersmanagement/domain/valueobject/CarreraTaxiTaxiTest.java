package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiTaxi (placa)")
class CarreraTaxiTaxiTest {

  @ParameterizedTest
  @ValueSource(strings = {"UAK512", "uak512", "UAK-512", " uak 512 ", "Uak-512"})
  @DisplayName("Normaliza la placa a mayúsculas sin espacios ni guiones")
  void shouldNormalizePlaca(final String value) {
    assertEquals("UAK512", new CarreraTaxiTaxi(value).value());
  }

  @Test
  @DisplayName("Rechaza una placa nula")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiTaxi(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "-", " - "})
  @DisplayName("Rechaza una placa vacía")
  void shouldRejectEmpty(final String value) {
    final InvalidCarreraTaxiTaxiException exception =
        assertThrows(InvalidCarreraTaxiTaxiException.class, () -> new CarreraTaxiTaxi(value));
    assertTrue(exception.getMessage().contains("vacía"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"AB123", "ABCD12", "123ABC", "ABC12", "ABC1234", "AB1234", "ÁBC123"})
  @DisplayName("Rechaza placas que no tienen el formato ABC123")
  void shouldRejectInvalidFormat(final String value) {
    final InvalidCarreraTaxiTaxiException exception =
        assertThrows(InvalidCarreraTaxiTaxiException.class, () -> new CarreraTaxiTaxi(value));
    assertTrue(exception.getMessage().contains("ABC123"));
  }
}
