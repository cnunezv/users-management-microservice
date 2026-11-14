package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetCarreraTaxiByIdUseCase {
  CarreraTaxiModel execute(@NotNull @Valid GetCarreraTaxiByIdQuery query);
}
