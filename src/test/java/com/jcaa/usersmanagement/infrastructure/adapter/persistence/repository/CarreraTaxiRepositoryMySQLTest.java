package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
import com.jcaa.usersmanagement.domain.model.CarreraTaxiModel;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiBarrio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCantidadPasajeros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiCliente;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiDuracionMinutos;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiId;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiKilometros;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiPrecio;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxi;
import com.jcaa.usersmanagement.domain.valueobject.CarreraTaxiTaxista;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests de CarreraTaxiRepositoryMySQL.
 *
 * <p>Cubre cada método público con sus ramas: flujo feliz, fila no encontrada y SQLException
 * traducida a PersistenceException.
 */
@DisplayName("CarreraTaxiRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class CarreraTaxiRepositoryMySQLTest {

  private static final String ID = "c-001";
  private static final String CLIENTE = "María Fernanda Pájaro";
  private static final String TAXI = "UAK512";
  private static final BigDecimal KILOMETROS = new BigDecimal("6.40");
  private static final String BARRIO_INICIO = "El Recreo";
  private static final String BARRIO_LLEGADA = "Centro Histórico";
  private static final int PASAJEROS = 2;
  private static final String TAXISTA = "Jorge Luis Marrugo";
  private static final BigDecimal PRECIO = new BigDecimal("14000.00");
  private static final int DURACION = 25;
  private static final String CREATED_AT = "2026-11-14 10:00:00";
  private static final String UPDATED_AT = "2026-11-14 10:05:00";

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private CarreraTaxiRepositoryMySQL repository;
  private final CarreraTaxiId carreraId = new CarreraTaxiId(ID);

  @BeforeEach
  void setUp() {
    repository = new CarreraTaxiRepositoryMySQL(dataSource);
  }

  private void configureStatement() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  private static CarreraTaxiModel sampleCarrera() {
    return new CarreraTaxiModel(
        new CarreraTaxiId(ID),
        new CarreraTaxiCliente(CLIENTE),
        new CarreraTaxiTaxi(TAXI),
        new CarreraTaxiKilometros(KILOMETROS),
        new CarreraTaxiBarrio(BARRIO_INICIO),
        new CarreraTaxiBarrio(BARRIO_LLEGADA),
        new CarreraTaxiCantidadPasajeros(PASAJEROS),
        new CarreraTaxiTaxista(TAXISTA),
        new CarreraTaxiPrecio(PRECIO),
        new CarreraTaxiDuracionMinutos(DURACION));
  }

  private void configureResultSetRow() throws SQLException {
    when(resultSet.getString("id")).thenReturn(ID);
    when(resultSet.getString("cliente")).thenReturn(CLIENTE);
    when(resultSet.getString("taxi")).thenReturn(TAXI);
    when(resultSet.getBigDecimal("kilometros")).thenReturn(KILOMETROS);
    when(resultSet.getString("barrio_inicio")).thenReturn(BARRIO_INICIO);
    when(resultSet.getString("barrio_llegada")).thenReturn(BARRIO_LLEGADA);
    when(resultSet.getInt("cantidad_pasajeros")).thenReturn(PASAJEROS);
    when(resultSet.getString("taxista")).thenReturn(TAXISTA);
    when(resultSet.getBigDecimal("precio")).thenReturn(PRECIO);
    when(resultSet.getInt("duracion_minutos")).thenReturn(DURACION);
    when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
    when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
  }

  // ── save

  @Test
  @DisplayName("save() inserta la carrera y la devuelve leída de la base")
  void shouldSaveCarrera() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final CarreraTaxiModel saved = repository.save(sampleCarrera());

    assertEquals(sampleCarrera(), saved);
    verify(statement).setString(1, ID);
    verify(statement).setBigDecimal(4, KILOMETROS);
    verify(statement).setInt(7, PASAJEROS);
    verify(statement).setBigDecimal(9, PRECIO);
    verify(statement).setInt(10, DURACION);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("save() traduce la SQLException del INSERT a PersistenceException")
  void shouldWrapSaveFailure() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));

    final CarreraTaxiModel carrera = sampleCarrera();
    final PersistenceException exception =
        assertThrows(PersistenceException.class, () -> repository.save(carrera));
    assertTrue(exception.getMessage().contains(ID));
  }

  @Test
  @DisplayName("save() lanza CarreraTaxiNotFoundException si no puede releer la fila")
  void shouldFailWhenSavedRowIsMissing() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    final CarreraTaxiModel carrera = sampleCarrera();
    assertThrows(CarreraTaxiNotFoundException.class, () -> repository.save(carrera));
  }

  // ── update

  @Test
  @DisplayName("update() actualiza la carrera usando el id en la cláusula WHERE")
  void shouldUpdateCarrera() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final CarreraTaxiModel updated = repository.update(sampleCarrera());

    assertEquals(sampleCarrera(), updated);
    verify(statement).setString(1, CLIENTE);
    verify(statement).setInt(9, DURACION);
    verify(statement).setString(10, ID);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("update() traduce la SQLException a PersistenceException")
  void shouldWrapUpdateFailure() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("boom"));

    final CarreraTaxiModel carrera = sampleCarrera();
    assertThrows(PersistenceException.class, () -> repository.update(carrera));
  }

  // ── getById

  @Test
  @DisplayName("getById() devuelve la carrera cuando existe")
  void shouldFindById() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final Optional<CarreraTaxiModel> found = repository.getById(carreraId);

    assertTrue(found.isPresent());
    assertEquals(sampleCarrera(), found.get());
    verify(statement).setString(1, ID);
  }

  @Test
  @DisplayName("getById() devuelve vacío cuando no existe")
  void shouldReturnEmptyWhenNotFound() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    assertTrue(repository.getById(carreraId).isEmpty());
  }

  @Test
  @DisplayName("getById() traduce la SQLException a PersistenceException")
  void shouldWrapFindByIdFailure() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));

    assertThrows(PersistenceException.class, () -> repository.getById(carreraId));
  }

  // ── getAll

  @Test
  @DisplayName("getAll() devuelve todas las carreras")
  void shouldGetAll() throws SQLException {
    configureStatement();
    when(statement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true, false);
    configureResultSetRow();

    final List<CarreraTaxiModel> carreras = repository.getAll();

    assertEquals(List.of(sampleCarrera()), carreras);
  }

  @Test
  @DisplayName("getAll() traduce la SQLException a PersistenceException")
  void shouldWrapGetAllFailure() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));

    assertThrows(PersistenceException.class, () -> repository.getAll());
  }

  // ── delete

  @Test
  @DisplayName("delete() ejecuta el DELETE con el id")
  void shouldDelete() throws SQLException {
    configureStatement();

    repository.delete(carreraId);

    verify(statement).setString(1, ID);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("delete() traduce la SQLException a PersistenceException")
  void shouldWrapDeleteFailure() throws SQLException {
    configureStatement();
    when(statement.executeUpdate()).thenThrow(new SQLException("boom"));

    assertThrows(PersistenceException.class, () -> repository.delete(carreraId));
  }
}
