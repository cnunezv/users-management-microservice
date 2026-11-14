package com.jcaa.usersmanagement.domain.exception;

public final class CarreraTaxiNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "No se encontró la carrera de taxi con id '%s'.";

  private CarreraTaxiNotFoundException(final String message) {
    super(message);
  }

  public static CarreraTaxiNotFoundException becauseIdWasNotFound(final String carreraId) {
    return new CarreraTaxiNotFoundException(String.format(MESSAGE_BY_ID, carreraId));
  }
}
