package com.jcaa.usersmanagement.domain.exception;

import java.math.BigDecimal;

public final class InvalidCarreraTaxiKilometrosException extends DomainException {

  private static final String MESSAGE_NOT_POSITIVE =
      "Los kilómetros de la carrera deben ser mayores que cero: %s.";
  private static final String MESSAGE_TOO_HIGH =
      "Los kilómetros de la carrera no pueden superar %s km.";

  private InvalidCarreraTaxiKilometrosException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiKilometrosException becauseValueIsNotPositive(
      final BigDecimal kilometros) {
    return new InvalidCarreraTaxiKilometrosException(
        String.format(MESSAGE_NOT_POSITIVE, kilometros.toPlainString()));
  }

  public static InvalidCarreraTaxiKilometrosException becauseValueIsTooHigh(
      final BigDecimal maximumKilometros) {
    return new InvalidCarreraTaxiKilometrosException(
        String.format(MESSAGE_TOO_HIGH, maximumKilometros.toPlainString()));
  }
}
