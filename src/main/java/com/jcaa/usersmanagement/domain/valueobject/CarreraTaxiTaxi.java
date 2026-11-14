package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxiException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Taxi que realiza la carrera, identificado por su placa.
 *
 * <p>Regla de negocio: placa colombiana de servicio público, tres letras y tres dígitos (ABC123).
 * Se normaliza a mayúsculas, sin espacios ni guiones.
 */
public record CarreraTaxiTaxi(String value) {

  private static final Pattern PLACA_PATTERN = Pattern.compile("^[A-Z]{3}[0-9]{3}$");

  public CarreraTaxiTaxi {
    final String normalizedValue =
        Objects.requireNonNull(value, "CarreraTaxiTaxi cannot be null")
            .trim()
            .replace(" ", "")
            .replace("-", "")
            .toUpperCase(Locale.ROOT);
    validateNotEmpty(normalizedValue);
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidCarreraTaxiTaxiException.becauseValueIsEmpty();
    }
  }

  private static void validateFormat(final String normalizedValue) {
    if (!PLACA_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidCarreraTaxiTaxiException.becauseFormatIsInvalid(normalizedValue);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
