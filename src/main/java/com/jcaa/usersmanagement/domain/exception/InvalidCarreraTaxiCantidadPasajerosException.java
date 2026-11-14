package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiCantidadPasajerosException extends DomainException {

  private static final String MESSAGE_OUT_OF_RANGE =
      "La cantidad de pasajeros debe estar entre %d y %d: %d.";

  private InvalidCarreraTaxiCantidadPasajerosException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiCantidadPasajerosException becauseValueIsOutOfRange(
      final int cantidad, final int minimum, final int maximum) {
    return new InvalidCarreraTaxiCantidadPasajerosException(
        String.format(MESSAGE_OUT_OF_RANGE, minimum, maximum, cantidad));
  }
}
