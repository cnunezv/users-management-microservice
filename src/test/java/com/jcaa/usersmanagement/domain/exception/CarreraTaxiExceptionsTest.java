package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Excepciones de dominio de CarreraTaxi")
class CarreraTaxiExceptionsTest {

  @Test
  @DisplayName("CarreraTaxiNotFoundException incluye el id buscado")
  void shouldBuildNotFound() {
    final CarreraTaxiNotFoundException exception =
        CarreraTaxiNotFoundException.becauseIdWasNotFound("c-404");

    assertInstanceOf(DomainException.class, exception);
    assertEquals("No se encontró la carrera de taxi con id 'c-404'.", exception.getMessage());
  }

  @Test
  @DisplayName("Las excepciones de rango incluyen los valores recibidos")
  void shouldFormatRangeMessages() {
    assertEquals(
        "La cantidad de pasajeros debe estar entre 1 y 4: 7.",
        InvalidCarreraTaxiCantidadPasajerosException.becauseValueIsOutOfRange(7, 1, 4)
            .getMessage());
    assertEquals(
        "La duración de la carrera debe ser mayor que cero minutos: 0.",
        InvalidCarreraTaxiDuracionMinutosException.becauseValueIsNotPositive(0).getMessage());
    assertEquals(
        "El precio de la carrera debe ser mayor que cero: -5.",
        InvalidCarreraTaxiPrecioException.becauseValueIsNotPositive(new BigDecimal("-5"))
            .getMessage());
    assertEquals(
        "Los kilómetros de la carrera no pueden superar 1000 km.",
        InvalidCarreraTaxiKilometrosException.becauseValueIsTooHigh(new BigDecimal("1000"))
            .getMessage());
  }

  @Test
  @DisplayName("La excepción de placa incluye el formato esperado")
  void shouldFormatPlacaMessage() {
    assertEquals(
        "La placa del taxi 'AB12' no es válida. Formato esperado: ABC123.",
        InvalidCarreraTaxiTaxiException.becauseFormatIsInvalid("AB12").getMessage());
  }

  @Test
  @DisplayName("Las excepciones de texto incluyen los límites de longitud")
  void shouldFormatLengthMessages() {
    assertEquals(
        "El nombre del cliente debe tener al menos 3 caracteres.",
        InvalidCarreraTaxiClienteException.becauseLengthIsTooShort(3).getMessage());
    assertEquals(
        "El nombre del taxista no puede superar los 100 caracteres.",
        InvalidCarreraTaxiTaxistaException.becauseLengthIsTooLong(100).getMessage());
    assertEquals(
        "El nombre del barrio no puede estar vacío.",
        InvalidCarreraTaxiBarrioException.becauseValueIsEmpty().getMessage());
    assertEquals(
        "El id de la carrera de taxi no puede estar vacío.",
        InvalidCarreraTaxiIdException.becauseValueIsEmpty().getMessage());
  }
}
