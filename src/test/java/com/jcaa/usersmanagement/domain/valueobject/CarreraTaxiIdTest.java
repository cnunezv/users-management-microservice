package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CarreraTaxiId")
class CarreraTaxiIdTest {

  @Test
  @DisplayName("Normaliza el id quitando espacios alrededor")
  void shouldTrimId() {
    final CarreraTaxiId id = new CarreraTaxiId("  c-001  ");
    assertEquals("c-001", id.value());
    assertEquals("c-001", id.toString());
  }

  @Test
  @DisplayName("Dos ids con el mismo valor son iguales")
  void shouldBeEqualWhenValuesMatch() {
    assertEquals(new CarreraTaxiId("c-001"), new CarreraTaxiId(" c-001"));
  }

  @Test
  @DisplayName("Rechaza un id nulo")
  void shouldRejectNull() {
    assertThrows(NullPointerException.class, () -> new CarreraTaxiId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n"})
  @DisplayName("Rechaza un id vacío")
  void shouldRejectEmpty(final String value) {
    assertThrows(InvalidCarreraTaxiIdException.class, () -> new CarreraTaxiId(value));
  }
}
