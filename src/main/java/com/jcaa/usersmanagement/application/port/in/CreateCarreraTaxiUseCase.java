package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateCarreraTaxiUseCase {
  CarreraTaxiModel execute(@NotNull @Valid CreateCarreraTaxiCommand command);
}
