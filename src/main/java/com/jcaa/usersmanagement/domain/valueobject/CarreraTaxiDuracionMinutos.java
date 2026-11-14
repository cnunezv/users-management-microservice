package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiDuracionMinutosException;

/** Duración de la carrera, en minutos enteros. */
public record CarreraTaxiDuracionMinutos(int value) {

  public CarreraTaxiDuracionMinutos {
    validatePositive(value);
  }

  private static void validatePositive(final int value) {
    if (value <= 0) {
      throw InvalidCarreraTaxiDuracionMinutosException.becauseValueIsNotPositive(value);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
