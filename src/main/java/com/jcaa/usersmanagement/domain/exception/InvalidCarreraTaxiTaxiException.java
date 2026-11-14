package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiTaxiException extends DomainException {

  private static final String MESSAGE_EMPTY = "La placa del taxi no puede estar vacía.";
  private static final String MESSAGE_INVALID_FORMAT =
      "La placa del taxi '%s' no es válida. Formato esperado: ABC123.";

  private InvalidCarreraTaxiTaxiException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiTaxiException becauseValueIsEmpty() {
    return new InvalidCarreraTaxiTaxiException(MESSAGE_EMPTY);
  }

  public static InvalidCarreraTaxiTaxiException becauseFormatIsInvalid(final String placa) {
    return new InvalidCarreraTaxiTaxiException(String.format(MESSAGE_INVALID_FORMAT, placa));
  }
}
