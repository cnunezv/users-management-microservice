package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiBarrioException extends DomainException {

  private static final String MESSAGE_EMPTY = "El nombre del barrio no puede estar vacío.";
  private static final String MESSAGE_TOO_SHORT = "El nombre del barrio debe tener al menos %d caracteres.";
  private static final String MESSAGE_TOO_LONG = "El nombre del barrio no puede superar los %d caracteres.";

  private InvalidCarreraTaxiBarrioException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiBarrioException becauseValueIsEmpty() {
    return new InvalidCarreraTaxiBarrioException(MESSAGE_EMPTY);
  }

  public static InvalidCarreraTaxiBarrioException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidCarreraTaxiBarrioException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }

  public static InvalidCarreraTaxiBarrioException becauseLengthIsTooLong(final int maximumLength) {
    return new InvalidCarreraTaxiBarrioException(String.format(MESSAGE_TOO_LONG, maximumLength));
  }
}
