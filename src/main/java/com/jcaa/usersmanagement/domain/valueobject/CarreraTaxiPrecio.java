package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiPrecioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Precio cobrado por la carrera, en pesos colombianos (COP), con dos decimales. */
public record CarreraTaxiPrecio(BigDecimal value) {

  private static final int SCALE = 2;

  public CarreraTaxiPrecio {
    Objects.requireNonNull(value, "CarreraTaxiPrecio cannot be null");
    validatePositive(value);
    value = value.setScale(SCALE, RoundingMode.HALF_UP);
  }

  private static void validatePositive(final BigDecimal value) {
    if (value.signum() <= 0) {
      throw InvalidCarreraTaxiPrecioException.becauseValueIsNotPositive(value);
    }
  }

  @Override
  public String toString() {
    return value.toPlainString();
  }
}
