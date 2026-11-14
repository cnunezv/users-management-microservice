package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiDuracionMinutosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiDuracionMinutos")
class CarreraTaxiDuracionMinutosTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 25, 180})
  @DisplayName("Acepta una duración positiva")
  void shouldAcceptPositive(final int value) {
    final CarreraTaxiDuracionMinutos duracion = new CarreraTaxiDuracionMinutos(value);
    assertEquals(value, duracion.value());
    assertEquals(String.valueOf(value), duracion.toString());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -30})
  @DisplayName("Rechaza una duración en cero o negativa")
  void shouldRejectNotPositive(final int value) {
    final InvalidCarreraTaxiDuracionMinutosException exception =
        assertThrows(
            InvalidCarreraTaxiDuracionMinutosException.class,
            () -> new CarreraTaxiDuracionMinutos(value));
    assertTrue(exception.getMessage().contains("mayor que cero"));
  }
}
