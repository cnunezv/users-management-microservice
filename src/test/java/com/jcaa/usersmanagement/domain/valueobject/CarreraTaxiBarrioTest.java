package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiBarrioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiBarrio")
class CarreraTaxiBarrioTest {

  @ParameterizedTest
  @ValueSource(strings = {"El Recreo", "   El Recreo   ", "El Recreo\t"})
  @DisplayName("Acepta un valor válido y lo normaliza")
  void shouldAcceptAndTrimValidValue(final String value) {
    assertEquals("El Recreo", new CarreraTaxiBarrio(value).value());
  }

  @Test
  @DisplayName("Acepta exactamente 3 y 100 caracteres")
  void shouldAcceptLengthLimits() {
    assertEquals("Abc", new CarreraTaxiBarrio("Abc").value());
    assertEquals(100, new CarreraTaxiBarrio("a".repeat(100)).value().length());
  }

  @Test
  @DisplayName("Rechaza un valor nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiBarrio(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t"})
  @DisplayName("Rechaza un valor vacío")
  void shouldRejectEmpty(final String value) {
    final InvalidCarreraTaxiBarrioException exception =
        assertThrows(InvalidCarreraTaxiBarrioException.class, () -> new CarreraTaxiBarrio(value));
    assertTrue(exception.getMessage().contains("vacío"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Bo", "A", "  Bo  "})
  @DisplayName("Rechaza menos de 3 caracteres")
  void shouldRejectTooShort(final String value) {
    final InvalidCarreraTaxiBarrioException exception =
        assertThrows(InvalidCarreraTaxiBarrioException.class, () -> new CarreraTaxiBarrio(value));
    assertTrue(exception.getMessage().contains("al menos 3"));
  }

  @Test
  @DisplayName("Rechaza más de 100 caracteres")
  void shouldRejectTooLong() {
    final String value = "a".repeat(101);
    final InvalidCarreraTaxiBarrioException exception =
        assertThrows(InvalidCarreraTaxiBarrioException.class, () -> new CarreraTaxiBarrio(value));
    assertTrue(exception.getMessage().contains("100"));
  }
}
