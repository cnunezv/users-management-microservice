package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.CarreraTaxiPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.CarreraTaxiEntity;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CarreraTaxiPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class CarreraTaxiPersistenceMapperTest {

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

  @Mock private ResultSet resultSet;

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

  @Test
  @DisplayName("fromModelToDto() copia los valores primitivos del modelo")
  void shouldMapModelToDto() {
    final CarreraTaxiPersistenceDto dto =
        CarreraTaxiPersistenceMapper.fromModelToDto(sampleCarrera());

    assertEquals(ID, dto.id());
    assertEquals(CLIENTE, dto.cliente());
    assertEquals(TAXI, dto.taxi());
    assertEquals(KILOMETROS, dto.kilometros());
    assertEquals(BARRIO_INICIO, dto.barrioInicio());
    assertEquals(BARRIO_LLEGADA, dto.barrioLlegada());
    assertEquals(PASAJEROS, dto.cantidadPasajeros());
    assertEquals(TAXISTA, dto.taxista());
    assertEquals(PRECIO, dto.precio());
    assertEquals(DURACION, dto.duracionMinutos());
    assertNull(dto.createdAt());
    assertNull(dto.updatedAt());
  }

  @Test
  @DisplayName("fromResultSetToEntity() lee cada columna de la fila")
  void shouldMapResultSetToEntity() throws SQLException {
    configureResultSetRow();

    final CarreraTaxiEntity entity = CarreraTaxiPersistenceMapper.fromResultSetToEntity(resultSet);

    assertEquals(ID, entity.id());
    assertEquals(BARRIO_LLEGADA, entity.barrioLlegada());
    assertEquals(PRECIO, entity.precio());
    assertEquals(CREATED_AT, entity.createdAt());
    assertEquals(UPDATED_AT, entity.updatedAt());
  }

  @Test
  @DisplayName("fromResultSetToModel() reconstruye el modelo de dominio")
  void shouldMapResultSetToModel() throws SQLException {
    configureResultSetRow();

    assertEquals(sampleCarrera(), CarreraTaxiPersistenceMapper.fromResultSetToModel(resultSet));
  }

  @Test
  @DisplayName("fromResultSetToModelList() recorre todas las filas")
  void shouldMapAllRows() throws SQLException {
    when(resultSet.next()).thenReturn(true, true, false);
    configureResultSetRow();

    final List<CarreraTaxiModel> carreras =
        CarreraTaxiPersistenceMapper.fromResultSetToModelList(resultSet);

    assertEquals(2, carreras.size());
  }

  @Test
  @DisplayName("fromResultSetToModelList() devuelve una lista vacía sin filas")
  void shouldReturnEmptyListWithoutRows() throws SQLException {
    when(resultSet.next()).thenReturn(false);

    assertTrue(CarreraTaxiPersistenceMapper.fromResultSetToModelList(resultSet).isEmpty());
  }
}
