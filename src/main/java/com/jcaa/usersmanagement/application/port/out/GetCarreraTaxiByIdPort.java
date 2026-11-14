package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import java.util.Optional;

public interface GetCarreraTaxiByIdPort {
  Optional<CarreraTaxiModel> getById(CarreraTaxiId carreraId);
}
