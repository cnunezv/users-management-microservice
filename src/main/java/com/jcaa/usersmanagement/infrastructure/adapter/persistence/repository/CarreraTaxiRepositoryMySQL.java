package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteCarreraTaxiPort;
import com.jcaa.usersmanagement.application.port.out.GetAllCarrerasTaxiPort;
import com.jcaa.usersmanagement.application.port.out.GetCarreraTaxiByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveCarreraTaxiPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCarreraTaxiPort;
import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.CarreraTaxiPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.CarreraTaxiPersistenceMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Adaptador de salida: implementa los puertos de persistencia de CarreraTaxi sobre MySQL con JDBC
 * y sentencias preparadas.
 */
@Repository
@RequiredArgsConstructor
public class CarreraTaxiRepositoryMySQL
    implements SaveCarreraTaxiPort,
        UpdateCarreraTaxiPort,
        GetCarreraTaxiByIdPort,
        GetAllCarrerasTaxiPort,
        DeleteCarreraTaxiPort {

  private static final String COLUMNS =
      "id, cliente, taxi, kilometros, barrio_inicio, barrio_llegada, cantidad_pasajeros, "
          + "taxista, precio, duracion_minutos, created_at, updated_at";

  private static final String SQL_INSERT =
      "INSERT INTO carreras_taxi "
          + "(id, cliente, taxi, kilometros, barrio_inicio, barrio_llegada, cantidad_pasajeros, "
          + "taxista, precio, duracion_minutos, created_at, updated_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE carreras_taxi SET cliente = ?, taxi = ?, kilometros = ?, barrio_inicio = ?, "
          + "barrio_llegada = ?, cantidad_pasajeros = ?, taxista = ?, precio = ?, "
          + "duracion_minutos = ?, updated_at = NOW() "
          + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT " + COLUMNS + " FROM carreras_taxi WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT " + COLUMNS + " FROM carreras_taxi ORDER BY created_at DESC, cliente ASC";

  private static final String SQL_DELETE = "DELETE FROM carreras_taxi WHERE id = ?";

  private final DataSource dataSource;

  @Override
  public CarreraTaxiModel save(final CarreraTaxiModel carrera) {
    final CarreraTaxiPersistenceDto dto = CarreraTaxiPersistenceMapper.fromModelToDto(carrera);
    executeSave(dto);
    return findByIdOrFail(carrera.getId());
  }

  @Override
  public CarreraTaxiModel update(final CarreraTaxiModel carrera) {
    final CarreraTaxiPersistenceDto dto = CarreraTaxiPersistenceMapper.fromModelToDto(carrera);
    executeUpdate(dto);
    return findByIdOrFail(carrera.getId());
  }

  @Override
  public Optional<CarreraTaxiModel> getById(final CarreraTaxiId carreraId) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, carreraId.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(CarreraTaxiPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseCarreraFindByIdFailed(carreraId.value(), exception);
    }
  }

  @Override
  public List<CarreraTaxiModel> getAll() {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return CarreraTaxiPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseCarreraFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final CarreraTaxiId carreraId) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, carreraId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseCarreraDeleteFailed(carreraId.value(), exception);
    }
  }

  private void executeSave(final CarreraTaxiPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.cliente());
      statement.setString(3, dto.taxi());
      statement.setBigDecimal(4, dto.kilometros());
      statement.setString(5, dto.barrioInicio());
      statement.setString(6, dto.barrioLlegada());
      statement.setInt(7, dto.cantidadPasajeros());
      statement.setString(8, dto.taxista());
      statement.setBigDecimal(9, dto.precio());
      statement.setInt(10, dto.duracionMinutos());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseCarreraSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final CarreraTaxiPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
        final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.cliente());
      statement.setString(2, dto.taxi());
      statement.setBigDecimal(3, dto.kilometros());
      statement.setString(4, dto.barrioInicio());
      statement.setString(5, dto.barrioLlegada());
      statement.setInt(6, dto.cantidadPasajeros());
      statement.setString(7, dto.taxista());
      statement.setBigDecimal(8, dto.precio());
      statement.setInt(9, dto.duracionMinutos());
      statement.setString(10, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseCarreraUpdateFailed(dto.id(), exception);
    }
  }

  private CarreraTaxiModel findByIdOrFail(final CarreraTaxiId carreraId) {
    return getById(carreraId)
        .orElseThrow(() -> CarreraTaxiNotFoundException.becauseIdWasNotFound(carreraId.value()));
  }
}
