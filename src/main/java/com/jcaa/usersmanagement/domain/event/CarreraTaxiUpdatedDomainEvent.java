package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class CarreraTaxiUpdatedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "carrera_taxi.updated";

  private final CarreraTaxiModel carrera;

  public CarreraTaxiUpdatedDomainEvent(final CarreraTaxiModel carrera) {
    super(EVENT_NAME);
    this.carrera = carrera;
  }

  @Override
  public Map<String, String> payload() {
    return Map.ofEntries(
        Map.entry("id", carrera.getId().value()),
        Map.entry("cliente", carrera.getCliente().value()),
        Map.entry("taxi", carrera.getTaxi().value()),
        Map.entry("kilometros", carrera.getKilometros().toString()),
        Map.entry("barrioInicio", carrera.getBarrioInicio().value()),
        Map.entry("barrioLlegada", carrera.getBarrioLlegada().value()),
        Map.entry("cantidadPasajeros", carrera.getCantidadPasajeros().toString()),
        Map.entry("taxista", carrera.getTaxista().value()),
        Map.entry("precio", carrera.getPrecio().toString()),
        Map.entry("duracionMinutos", carrera.getDuracionMinutos().toString()));
  }
}
