package com.jcaa.usersmanagement.domain.exception;

import java.math.BigDecimal;

public final class InvalidCarreraTaxiPrecioException extends DomainException {

  private static final String MESSAGE_NOT_POSITIVE =
      "El precio de la carrera debe ser mayor que cero: %s.";

  private InvalidCarreraTaxiPrecioException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiPrecioException becauseValueIsNotPositive(
      final BigDecimal precio) {
    return new InvalidCarreraTaxiPrecioException(
        String.format(MESSAGE_NOT_POSITIVE, precio.toPlainString()));
  }
}
