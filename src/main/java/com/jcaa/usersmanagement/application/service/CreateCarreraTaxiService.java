package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.out.SaveCarreraTaxiPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.mapper.CarreraTaxiApplicationMapper;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCarreraTaxiService implements CreateCarreraTaxiUseCase {

  private final SaveCarreraTaxiPort saveCarreraTaxiPort;
  private final Validator validator;

  @Override
  public CarreraTaxiModel execute(final CreateCarreraTaxiCommand command) {
    validate(command);

    final CarreraTaxiModel carreraToSave =
        CarreraTaxiApplicationMapper.fromCreateCommandToModel(command);
    return saveCarreraTaxiPort.save(carreraToSave);
  }

  private void validate(final CreateCarreraTaxiCommand value) {
    final Set<ConstraintViolation<CreateCarreraTaxiCommand>> violations = validator.validate(value);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
