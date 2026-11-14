package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiDuracionMinutosException extends DomainException {

  private static final String MESSAGE_NOT_POSITIVE =
      "La duración de la carrera debe ser mayor que cero minutos: %d.";

  private InvalidCarreraTaxiDuracionMinutosException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiDuracionMinutosException becauseValueIsNotPositive(
      final int duracionMinutos) {
    return new InvalidCarreraTaxiDuracionMinutosException(
        String.format(MESSAGE_NOT_POSITIVE, duracionMinutos));
  }
}
