package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiKilometrosException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Distancia recorrida en la carrera, en kilómetros, con dos decimales. */
public record CarreraTaxiKilometros(BigDecimal value) {

  private static final int SCALE = 2;
  private static final BigDecimal MAXIMUM_KILOMETROS = new BigDecimal("1000");

  public CarreraTaxiKilometros {
    Objects.requireNonNull(value, "CarreraTaxiKilometros cannot be null");
    validatePositive(value);
    validateMaximum(value);
    value = value.setScale(SCALE, RoundingMode.HALF_UP);
  }

  private static void validatePositive(final BigDecimal value) {
    if (value.signum() <= 0) {
      throw InvalidCarreraTaxiKilometrosException.becauseValueIsNotPositive(value);
    }
  }

  private static void validateMaximum(final BigDecimal value) {
    if (value.compareTo(MAXIMUM_KILOMETROS) > 0) {
      throw InvalidCarreraTaxiKilometrosException.becauseValueIsTooHigh(MAXIMUM_KILOMETROS);
    }
  }

  @Override
  public String toString() {
    return value.toPlainString();
  }
}
