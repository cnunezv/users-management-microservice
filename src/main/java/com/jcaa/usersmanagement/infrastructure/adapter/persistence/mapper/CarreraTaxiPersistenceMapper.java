package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarreraTaxiPersistenceMapper {

  public CarreraTaxiPersistenceDto fromModelToDto(final CarreraTaxiModel carrera) {
    return new CarreraTaxiPersistenceDto(
        carrera.getId().value(),
        carrera.getCliente().value(),
        carrera.getTaxi().value(),
        carrera.getKilometros().value(),
        carrera.getBarrioInicio().value(),
        carrera.getBarrioLlegada().value(),
        carrera.getCantidadPasajeros().value(),
        carrera.getTaxista().value(),
        carrera.getPrecio().value(),
        carrera.getDuracionMinutos().value(),
        null,
        null);
  }

  public CarreraTaxiEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new CarreraTaxiEntity(
        resultSet.getString("id"),
        resultSet.getString("cliente"),
        resultSet.getString("taxi"),
        resultSet.getBigDecimal("kilometros"),
        resultSet.getString("barrio_inicio"),
        resultSet.getString("barrio_llegada"),
        resultSet.getInt("cantidad_pasajeros"),
        resultSet.getString("taxista"),
        resultSet.getBigDecimal("precio"),
        resultSet.getInt("duracion_minutos"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public CarreraTaxiModel fromEntityToModel(final CarreraTaxiEntity entity) {
    return new CarreraTaxiModel(
        new CarreraTaxiId(entity.id()),
        new CarreraTaxiCliente(entity.cliente()),
        new CarreraTaxiTaxi(entity.taxi()),
        new CarreraTaxiKilometros(entity.kilometros()),
        new CarreraTaxiBarrio(entity.barrioInicio()),
        new CarreraTaxiBarrio(entity.barrioLlegada()),
        new CarreraTaxiCantidadPasajeros(entity.cantidadPasajeros()),
        new CarreraTaxiTaxista(entity.taxista()),
        new CarreraTaxiPrecio(entity.precio()),
        new CarreraTaxiDuracionMinutos(entity.duracionMinutos()));
  }

  public CarreraTaxiModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<CarreraTaxiModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<CarreraTaxiModel> carreras = new ArrayList<>();
    while (resultSet.next()) {
      carreras.add(fromResultSetToModel(resultSet));
    }
    return carreras;
  }
}
