package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateCarreraTaxiUseCase {
  CarreraTaxiModel execute(@NotNull @Valid UpdateCarreraTaxiCommand command);
}
