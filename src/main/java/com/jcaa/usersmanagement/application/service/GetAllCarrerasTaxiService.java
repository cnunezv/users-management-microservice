package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllCarrerasTaxiUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllCarrerasTaxiPort;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllCarrerasTaxiService implements GetAllCarrerasTaxiUseCase {

  private final GetAllCarrerasTaxiPort getAllCarrerasTaxiPort;

  @Override
  public List<CarreraTaxiModel> execute() {
    return getAllCarrerasTaxiPort.getAll();
  }
}
