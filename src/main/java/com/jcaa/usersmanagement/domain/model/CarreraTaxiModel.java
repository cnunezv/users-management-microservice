package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiBarrio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCantidadPasajeros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCliente;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiDuracionMinutos;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiKilometros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiPrecio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxi;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxista;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import lombok.Value;

/**
 * Modelo de dominio del ejercicio 19: una carrera (servicio) de taxi.
 *
 * <p>Es inmutable y nunca puede existir en un estado inválido, porque cada atributo es un value
 * object que se valida al construirse.
 */
@Value
public class CarreraTaxiModel {

  private static final int RESULT_SCALE = 2;
  private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal("60");

  CarreraTaxiId id;
  CarreraTaxiCliente cliente;
  CarreraTaxiTaxi taxi;
  CarreraTaxiKilometros kilometros;
  CarreraTaxiBarrio barrioInicio;
  CarreraTaxiBarrio barrioLlegada;
  CarreraTaxiCantidadPasajeros cantidadPasajeros;
  CarreraTaxiTaxista taxista;
  CarreraTaxiPrecio precio;
  CarreraTaxiDuracionMinutos duracionMinutos;

  public static CarreraTaxiModel create(
      final CarreraTaxiId id,
      final CarreraTaxiCliente cliente,
      final CarreraTaxiTaxi taxi,
      final CarreraTaxiKilometros kilometros,
      final CarreraTaxiBarrio barrioInicio,
      final CarreraTaxiBarrio barrioLlegada,
      final CarreraTaxiCantidadPasajeros cantidadPasajeros,
      final CarreraTaxiTaxista taxista,
      final CarreraTaxiPrecio precio,
      final CarreraTaxiDuracionMinutos duracionMinutos) {
    return new CarreraTaxiModel(
        id,
        cliente,
        taxi,
        kilometros,
        barrioInicio,
        barrioLlegada,
        cantidadPasajeros,
        taxista,
        precio,
        duracionMinutos);
  }

  /** Precio cobrado por cada kilómetro recorrido, en COP. */
  public BigDecimal precioPorKilometro() {
    return precio.value().divide(kilometros.value(), RESULT_SCALE, RoundingMode.HALF_UP);
  }

  /** Velocidad promedio de la carrera, en km/h. */
  public BigDecimal velocidadPromedioKmH() {
    return kilometros
        .value()
        .multiply(MINUTES_PER_HOUR)
        .divide(BigDecimal.valueOf(duracionMinutos.value()), RESULT_SCALE, RoundingMode.HALF_UP);
  }

  /** Indica si la carrera empezó y terminó en el mismo barrio. */
  public boolean esDentroDelMismoBarrio() {
    return barrioInicio
        .value()
        .toLowerCase(Locale.ROOT)
        .equals(barrioLlegada.value().toLowerCase(Locale.ROOT));
  }

  public CarreraTaxiModel changePrecio(final CarreraTaxiPrecio nuevoPrecio) {
    return new CarreraTaxiModel(
        id,
        cliente,
        taxi,
        kilometros,
        barrioInicio,
        barrioLlegada,
        cantidadPasajeros,
        taxista,
        nuevoPrecio,
        duracionMinutos);
  }
}
