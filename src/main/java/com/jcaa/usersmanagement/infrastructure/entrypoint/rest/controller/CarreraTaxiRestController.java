package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCarrerasTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCarreraTaxiByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCarreraTaxiRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CarreraTaxiRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.CarreraTaxiRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada REST del CRUDL de carreras de taxi (ejercicio 19). */
@RestController
@RequestMapping("/api/carreras-taxi")
@RequiredArgsConstructor
public class CarreraTaxiRestController implements CarreraTaxiRestControllerDocs {

  private final CreateCarreraTaxiUseCase createCarreraTaxiUseCase;
  private final UpdateCarreraTaxiUseCase updateCarreraTaxiUseCase;
  private final DeleteCarreraTaxiUseCase deleteCarreraTaxiUseCase;
  private final GetCarreraTaxiByIdUseCase getCarreraTaxiByIdUseCase;
  private final GetAllCarrerasTaxiUseCase getAllCarrerasTaxiUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CarreraTaxiRestResponse create(
      @Valid @RequestBody final CreateCarreraTaxiRestRequest request) {
    final CarreraTaxiModel carrera =
        createCarreraTaxiUseCase.execute(CarreraTaxiRestMapper.toCreateCommand(request));
    return CarreraTaxiRestMapper.toResponse(carrera);
  }

  @GetMapping
  public List<CarreraTaxiRestResponse> getAll() {
    return CarreraTaxiRestMapper.toResponseList(getAllCarrerasTaxiUseCase.execute());
  }

  @GetMapping("/{id}")
  public CarreraTaxiRestResponse getById(@PathVariable final String id) {
    final CarreraTaxiModel carrera =
        getCarreraTaxiByIdUseCase.execute(CarreraTaxiRestMapper.toGetByIdQuery(id));
    return CarreraTaxiRestMapper.toResponse(carrera);
  }

  @PutMapping("/{id}")
  public CarreraTaxiRestResponse update(
      @PathVariable final String id,
      @Valid @RequestBody final UpdateCarreraTaxiRestRequest request) {
    final CarreraTaxiModel carrera =
        updateCarreraTaxiUseCase.execute(CarreraTaxiRestMapper.toUpdateCommand(id, request));
    return CarreraTaxiRestMapper.toResponse(carrera);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    deleteCarreraTaxiUseCase.execute(CarreraTaxiRestMapper.toDeleteCommand(id));
  }
}
