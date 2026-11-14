package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiCantidadPasajerosException;

/**
 * Cantidad de pasajeros transportados.
 *
 * <p>Regla de negocio: un taxi lleva como mínimo 1 y como máximo 4 pasajeros.
 */
public record CarreraTaxiCantidadPasajeros(int value) {

  public static final int MINIMUM = 1;
  public static final int MAXIMUM = 4;

  public CarreraTaxiCantidadPasajeros {
    validateRange(value);
  }

  private static void validateRange(final int value) {
    if (value < MINIMUM || value > MAXIMUM) {
      throw InvalidCarreraTaxiCantidadPasajerosException.becauseValueIsOutOfRange(
          value, MINIMUM, MAXIMUM);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
