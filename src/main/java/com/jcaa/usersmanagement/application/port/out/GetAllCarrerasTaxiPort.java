package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import java.util.List;

public interface GetAllCarrerasTaxiPort {
  List<CarreraTaxiModel> getAll();
}
