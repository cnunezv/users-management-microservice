package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiClienteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiCliente")
class CarreraTaxiClienteTest {

  @ParameterizedTest
  @ValueSource(strings = {"María Fernanda Pájaro", "   María Fernanda Pájaro   ", "María Fernanda Pájaro\t"})
  @DisplayName("Acepta un valor válido y lo normaliza")
  void shouldAcceptAndTrimValidValue(final String value) {
    assertEquals("María Fernanda Pájaro", new CarreraTaxiCliente(value).value());
  }

  @Test
  @DisplayName("Acepta exactamente 3 y 100 caracteres")
  void shouldAcceptLengthLimits() {
    assertEquals("Abc", new CarreraTaxiCliente("Abc").value());
    assertEquals(100, new CarreraTaxiCliente("a".repeat(100)).value().length());
  }

  @Test
  @DisplayName("Rechaza un valor nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiCliente(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t"})
  @DisplayName("Rechaza un valor vacío")
  void shouldRejectEmpty(final String value) {
    final InvalidCarreraTaxiClienteException exception =
        assertThrows(InvalidCarreraTaxiClienteException.class, () -> new CarreraTaxiCliente(value));
    assertTrue(exception.getMessage().contains("vacío"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Al", "A", "  Al  "})
  @DisplayName("Rechaza menos de 3 caracteres")
  void shouldRejectTooShort(final String value) {
    final InvalidCarreraTaxiClienteException exception =
        assertThrows(InvalidCarreraTaxiClienteException.class, () -> new CarreraTaxiCliente(value));
    assertTrue(exception.getMessage().contains("al menos 3"));
  }

  @Test
  @DisplayName("Rechaza más de 100 caracteres")
  void shouldRejectTooLong() {
    final String value = "a".repeat(101);
    final InvalidCarreraTaxiClienteException exception =
        assertThrows(InvalidCarreraTaxiClienteException.class, () -> new CarreraTaxiCliente(value));
    assertTrue(exception.getMessage().contains("100"));
  }
}
