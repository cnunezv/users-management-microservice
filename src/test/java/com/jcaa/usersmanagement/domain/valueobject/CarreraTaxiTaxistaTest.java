package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxistaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiTaxista")
class CarreraTaxiTaxistaTest {

  @ParameterizedTest
  @ValueSource(strings = {"Jorge Luis Marrugo", "   Jorge Luis Marrugo   ", "Jorge Luis Marrugo\t"})
  @DisplayName("Acepta un valor válido y lo normaliza")
  void shouldAcceptAndTrimValidValue(final String value) {
    assertEquals("Jorge Luis Marrugo", new CarreraTaxiTaxista(value).value());
  }

  @Test
  @DisplayName("Acepta exactamente 3 y 100 caracteres")
  void shouldAcceptLengthLimits() {
    assertEquals("Abc", new CarreraTaxiTaxista("Abc").value());
    assertEquals(100, new CarreraTaxiTaxista("a".repeat(100)).value().length());
  }

  @Test
  @DisplayName("Rechaza un valor nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiTaxista(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t"})
  @DisplayName("Rechaza un valor vacío")
  void shouldRejectEmpty(final String value) {
    final InvalidCarreraTaxiTaxistaException exception =
        assertThrows(InvalidCarreraTaxiTaxistaException.class, () -> new CarreraTaxiTaxista(value));
    assertTrue(exception.getMessage().contains("vacío"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Jo", "A", "  Jo  "})
  @DisplayName("Rechaza menos de 3 caracteres")
  void shouldRejectTooShort(final String value) {
    final InvalidCarreraTaxiTaxistaException exception =
        assertThrows(InvalidCarreraTaxiTaxistaException.class, () -> new CarreraTaxiTaxista(value));
    assertTrue(exception.getMessage().contains("al menos 3"));
  }

  @Test
  @DisplayName("Rechaza más de 100 caracteres")
  void shouldRejectTooLong() {
    final String value = "a".repeat(101);
    final InvalidCarreraTaxiTaxistaException exception =
        assertThrows(InvalidCarreraTaxiTaxistaException.class, () -> new CarreraTaxiTaxista(value));
    assertTrue(exception.getMessage().contains("100"));
  }
}
