package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import java.util.Map;
import lombok.Getter;

@Getter
public final class CarreraTaxiDeletedDomainEvent extends DomainEvent {

  private static final String EVENT_NAME = "carrera_taxi.deleted";

  private final CarreraTaxiId carreraId;

  public CarreraTaxiDeletedDomainEvent(final CarreraTaxiId carreraId) {
    super(EVENT_NAME);
    this.carreraId = carreraId;
  }

  @Override
  public Map<String, String> payload() {
    return Map.of("id", carreraId.value());
  }
}
