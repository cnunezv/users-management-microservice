package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetCarreraTaxiByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
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
public class GetCarreraTaxiByIdService implements GetCarreraTaxiByIdUseCase {

  private final GetCarreraTaxiByIdPort getCarreraTaxiByIdPort;
  private final Validator validator;

  @Override
  public CarreraTaxiModel execute(final GetCarreraTaxiByIdQuery query) {
    validate(query);

    final CarreraTaxiId carreraId =
        CarreraTaxiApplicationMapper.fromGetByIdQueryToCarreraTaxiId(query);
    return getCarreraTaxiByIdPort
        .getById(carreraId)
        .orElseThrow(() -> CarreraTaxiNotFoundException.becauseIdWasNotFound(carreraId.value()));
  }

  private void validate(final GetCarreraTaxiByIdQuery value) {
    final Set<ConstraintViolation<GetCarreraTaxiByIdQuery>> violations = validator.validate(value);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
