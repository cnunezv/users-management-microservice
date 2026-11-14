package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DeleteCarreraTaxiUseCase {
  void execute(@NotNull @Valid DeleteCarreraTaxiCommand command);
}
