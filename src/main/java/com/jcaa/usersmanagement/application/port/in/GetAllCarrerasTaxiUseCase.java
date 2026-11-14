package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import java.util.List;

public interface GetAllCarrerasTaxiUseCase {
  List<CarreraTaxiModel> execute();
}
