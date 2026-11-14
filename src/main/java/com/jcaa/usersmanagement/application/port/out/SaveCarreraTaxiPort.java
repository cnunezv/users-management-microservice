package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;

public interface SaveCarreraTaxiPort {
  CarreraTaxiModel save(CarreraTaxiModel carrera);
}
