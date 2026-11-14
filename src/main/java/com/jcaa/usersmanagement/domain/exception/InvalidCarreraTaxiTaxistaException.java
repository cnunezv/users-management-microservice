package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiTaxistaException extends DomainException {

  private static final String MESSAGE_EMPTY = "El nombre del taxista no puede estar vacío.";
  private static final String MESSAGE_TOO_SHORT = "El nombre del taxista debe tener al menos %d caracteres.";
  private static final String MESSAGE_TOO_LONG = "El nombre del taxista no puede superar los %d caracteres.";

  private InvalidCarreraTaxiTaxistaException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiTaxistaException becauseValueIsEmpty() {
    return new InvalidCarreraTaxiTaxistaException(MESSAGE_EMPTY);
  }

  public static InvalidCarreraTaxiTaxistaException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidCarreraTaxiTaxistaException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }

  public static InvalidCarreraTaxiTaxistaException becauseLengthIsTooLong(final int maximumLength) {
    return new InvalidCarreraTaxiTaxistaException(String.format(MESSAGE_TOO_LONG, maximumLength));
  }
}
