package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiPrecioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiPrecio")
class CarreraTaxiPrecioTest {

  @Test
  @DisplayName("Guarda el precio con dos decimales")
  void shouldUseTwoDecimals() {
    final CarreraTaxiPrecio precio = new CarreraTaxiPrecio(new BigDecimal("14000"));
    assertEquals(new BigDecimal("14000.00"), precio.value());
    assertEquals("14000.00", precio.toString());
  }

  @Test
  @DisplayName("Rechaza un valor nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiPrecio(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "-1", "-14000"})
  @DisplayName("Rechaza un precio en cero o negativo")
  void shouldRejectNotPositive(final String value) {
    final InvalidCarreraTaxiPrecioException exception =
        assertThrows(
            InvalidCarreraTaxiPrecioException.class,
            () -> new CarreraTaxiPrecio(new BigDecimal(value)));
    assertTrue(exception.getMessage().contains("mayor que cero"));
  }
}
