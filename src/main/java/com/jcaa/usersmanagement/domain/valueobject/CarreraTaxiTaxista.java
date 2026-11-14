package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxistaException;
import java.util.Objects;

/** Nombre del taxista que realiza la carrera. */
public record CarreraTaxiTaxista(String value) {

  private static final int MINIMUM_LENGTH = 3;
  private static final int MAXIMUM_LENGTH = 100;

  public CarreraTaxiTaxista {
    final String normalizedValue =
        Objects.requireNonNull(value, "CarreraTaxiTaxista cannot be null").trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    validateMaximumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidCarreraTaxiTaxistaException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidCarreraTaxiTaxistaException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }

  private static void validateMaximumLength(final String normalizedValue) {
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidCarreraTaxiTaxistaException.becauseLengthIsTooLong(MAXIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
