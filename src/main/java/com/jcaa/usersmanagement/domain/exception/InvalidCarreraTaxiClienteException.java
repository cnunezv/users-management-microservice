package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiClienteException extends DomainException {

  private static final String MESSAGE_EMPTY = "El nombre del cliente no puede estar vacío.";
  private static final String MESSAGE_TOO_SHORT = "El nombre del cliente debe tener al menos %d caracteres.";
  private static final String MESSAGE_TOO_LONG = "El nombre del cliente no puede superar los %d caracteres.";

  private InvalidCarreraTaxiClienteException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiClienteException becauseValueIsEmpty() {
    return new InvalidCarreraTaxiClienteException(MESSAGE_EMPTY);
  }

  public static InvalidCarreraTaxiClienteException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidCarreraTaxiClienteException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }

  public static InvalidCarreraTaxiClienteException becauseLengthIsTooLong(final int maximumLength) {
    return new InvalidCarreraTaxiClienteException(String.format(MESSAGE_TOO_LONG, maximumLength));
  }
}
