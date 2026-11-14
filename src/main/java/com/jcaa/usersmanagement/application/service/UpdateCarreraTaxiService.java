package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCarreraTaxiPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.mapper.CarreraTaxiApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCarreraTaxiService implements UpdateCarreraTaxiUseCase {

  private final UpdateCarreraTaxiPort updateCarreraTaxiPort;
  private final GetCarreraTaxiByIdPort getCarreraTaxiByIdPort;
  private final Validator validator;

  @Override
  public CarreraTaxiModel execute(final UpdateCarreraTaxiCommand command) {
    validate(command);

    final CarreraTaxiModel carreraToUpdate =
        CarreraTaxiApplicationMapper.fromUpdateCommandToModel(command);
    ensureCarreraExists(carreraToUpdate.getId());

    return updateCarreraTaxiPort.update(carreraToUpdate);
  }

  private void ensureCarreraExists(final CarreraTaxiId carreraId) {
    getCarreraTaxiByIdPort
        .getById(carreraId)
        .orElseThrow(() -> CarreraTaxiNotFoundException.becauseIdWasNotFound(carreraId.value()));
  }

  private void validate(final UpdateCarreraTaxiCommand value) {
    final Set<ConstraintViolation<UpdateCarreraTaxiCommand>> violations = validator.validate(value);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
