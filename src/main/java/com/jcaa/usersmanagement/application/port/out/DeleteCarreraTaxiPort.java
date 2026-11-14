package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;

public interface DeleteCarreraTaxiPort {
  void delete(CarreraTaxiId carreraId);
}
