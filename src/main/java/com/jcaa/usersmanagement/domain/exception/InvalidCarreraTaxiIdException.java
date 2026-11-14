package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCarreraTaxiIdException extends DomainException {

  private static final String MESSAGE_EMPTY = "El id de la carrera de taxi no puede estar vacío.";

  private InvalidCarreraTaxiIdException(final String message) {
    super(message);
  }

  public static InvalidCarreraTaxiIdException becauseValueIsEmpty() {
    return new InvalidCarreraTaxiIdException(MESSAGE_EMPTY);
  }
}
