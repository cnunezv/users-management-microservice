package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiBarrioException;
import java.util.Objects;

/** Nombre de un barrio de Cartagena. Se usa para el barrio de inicio y el de llegada. */
public record CarreraTaxiBarrio(String value) {

  private static final int MINIMUM_LENGTH = 3;
  private static final int MAXIMUM_LENGTH = 100;

  public CarreraTaxiBarrio {
    final String normalizedValue =
        Objects.requireNonNull(value, "CarreraTaxiBarrio cannot be null").trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    validateMaximumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidCarreraTaxiBarrioException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidCarreraTaxiBarrioException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }

  private static void validateMaximumLength(final String normalizedValue) {
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidCarreraTaxiBarrioException.becauseLengthIsTooLong(MAXIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
