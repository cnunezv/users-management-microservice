package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteCarreraTaxiPort;
import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.mapper.CarreraTaxiApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCarreraTaxiService implements DeleteCarreraTaxiUseCase {

  private final DeleteCarreraTaxiPort deleteCarreraTaxiPort;
  private final GetCarreraTaxiByIdPort getCarreraTaxiByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteCarreraTaxiCommand command) {
    validate(command);

    final CarreraTaxiId carreraId =
        CarreraTaxiApplicationMapper.fromDeleteCommandToCarreraTaxiId(command);
    ensureCarreraExists(carreraId);
    deleteCarreraTaxiPort.delete(carreraId);
  }

  private void ensureCarreraExists(final CarreraTaxiId carreraId) {
    getCarreraTaxiByIdPort
        .getById(carreraId)
        .orElseThrow(() -> CarreraTaxiNotFoundException.becauseIdWasNotFound(carreraId.value()));
  }

  private void validate(final DeleteCarreraTaxiCommand value) {
    final Set<ConstraintViolation<DeleteCarreraTaxiCommand>> violations = validator.validate(value);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
