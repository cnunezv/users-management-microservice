package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiIdException;
import java.util.Objects;

public record CarreraTaxiId(String value) {

  public CarreraTaxiId {
    final String normalizedValue =
        Objects.requireNonNull(value, "CarreraTaxiId cannot be null").trim();
    validateNotEmpty(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidCarreraTaxiIdException.becauseValueIsEmpty();
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
