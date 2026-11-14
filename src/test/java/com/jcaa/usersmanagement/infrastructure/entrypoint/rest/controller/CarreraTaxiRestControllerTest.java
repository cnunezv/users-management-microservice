package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.application.port.in.CreateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCarrerasTaxiUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCarreraTaxiByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCarreraTaxiUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCarreraTaxiCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCarreraTaxiByIdQuery;
import com.jcaa.usersmanagement.domain.exception.CarreraTaxiNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidCarreraTaxiTaxiException;
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
import com.jcaa.usersmanagement.infrastructure.security.JwtAuthenticationFilter;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenService;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import com.jcaa.usersmanagement.infrastructure.security.SecurityConfig;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CarreraTaxiRestController.class,
    properties = "spring.main.web-application-type=servlet")
@Import({
  SecurityConfig.class,
  JwtAuthenticationFilter.class,
  RestAuthenticationEntryPoint.class,
  RestAccessDeniedHandler.class
})
@DisplayName("CarreraTaxiRestController")
class CarreraTaxiRestControllerTest {

  private static final String ID = "10000000-0000-0000-0000-000000000001";
  private static final String BASE_PATH = "/api/carreras-taxi";

  private static final String VALID_BODY =
      """
      {
        "cliente": "María Fernanda Pájaro",
        "taxi": "UAK512",
        "kilometros": 6.4,
        "barrioInicio": "El Recreo",
        "barrioLlegada": "Centro Histórico",
        "cantidadPasajeros": 2,
        "taxista": "Jorge Luis Marrugo",
        "precio": 14000,
        "duracionMinutos": 25
      }
      """;

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateCarreraTaxiUseCase createCarreraTaxiUseCase;
  @MockBean private UpdateCarreraTaxiUseCase updateCarreraTaxiUseCase;
  @MockBean private DeleteCarreraTaxiUseCase deleteCarreraTaxiUseCase;
  @MockBean private GetCarreraTaxiByIdUseCase getCarreraTaxiByIdUseCase;
  @MockBean private GetAllCarrerasTaxiUseCase getAllCarrerasTaxiUseCase;
  @MockBean private JwtTokenService jwtTokenService;

  private static CarreraTaxiModel sampleCarrera() {
    return new CarreraTaxiModel(
        new CarreraTaxiId(ID),
        new CarreraTaxiCliente("María Fernanda Pájaro"),
        new CarreraTaxiTaxi("UAK512"),
        new CarreraTaxiKilometros(new BigDecimal("6.4")),
        new CarreraTaxiBarrio("El Recreo"),
        new CarreraTaxiBarrio("Centro Histórico"),
        new CarreraTaxiCantidadPasajeros(2),
        new CarreraTaxiTaxista("Jorge Luis Marrugo"),
        new CarreraTaxiPrecio(new BigDecimal("14000")),
        new CarreraTaxiDuracionMinutos(25));
  }

  // ── listar y consultar

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("GET /api/carreras-taxi lista las carreras con los valores calculados")
  void shouldListCarreras() throws Exception {
    when(getAllCarrerasTaxiUseCase.execute()).thenReturn(List.of(sampleCarrera()));

    mockMvc
        .perform(get(BASE_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ID))
        .andExpect(jsonPath("$[0].taxi").value("UAK512"))
        .andExpect(jsonPath("$[0].kilometros").value(6.4))
        .andExpect(jsonPath("$[0].cantidadPasajeros").value(2))
        .andExpect(jsonPath("$[0].precioPorKilometro").value(2187.5))
        .andExpect(jsonPath("$[0].velocidadPromedioKmH").value(15.36));
  }

  @Test
  @DisplayName("GET /api/carreras-taxi sin token responde 401")
  void shouldRequireAuthentication() throws Exception {
    mockMvc.perform(get(BASE_PATH)).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("GET /api/carreras-taxi/{id} devuelve la carrera")
  void shouldGetById() throws Exception {
    when(getCarreraTaxiByIdUseCase.execute(new GetCarreraTaxiByIdQuery(ID)))
        .thenReturn(sampleCarrera());

    mockMvc
        .perform(get(BASE_PATH + "/" + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cliente").value("María Fernanda Pájaro"))
        .andExpect(jsonPath("$.barrioLlegada").value("Centro Histórico"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/carreras-taxi/{id} responde 404 si la carrera no existe")
  void shouldReturnNotFound() throws Exception {
    when(getCarreraTaxiByIdUseCase.execute(new GetCarreraTaxiByIdQuery("no-existe")))
        .thenThrow(CarreraTaxiNotFoundException.becauseIdWasNotFound("no-existe"));

    mockMvc
        .perform(get(BASE_PATH + "/no-existe"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("No se encontró la carrera de taxi con id 'no-existe'."));
  }

  // ── registrar

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("POST /api/carreras-taxi registra una carrera y responde 201")
  void shouldCreateCarrera() throws Exception {
    when(createCarreraTaxiUseCase.execute(any())).thenReturn(sampleCarrera());

    mockMvc
        .perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.taxi").value("UAK512"));
  }

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("POST /api/carreras-taxi con rol REVIEWER responde 403")
  void shouldForbidCreateForReviewer() throws Exception {
    mockMvc
        .perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isForbidden());
    verify(createCarreraTaxiUseCase, never()).execute(any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/carreras-taxi con campos faltantes responde 400")
  void shouldRejectMissingFields() throws Exception {
    mockMvc
        .perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
    verify(createCarreraTaxiUseCase, never()).execute(any());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/carreras-taxi con JSON mal formado responde 400")
  void shouldRejectMalformedJson() throws Exception {
    mockMvc
        .perform(
            post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"kilometros\": \"muchos\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/carreras-taxi responde 400 cuando se incumple una regla de dominio")
  void shouldReturnBadRequestOnDomainRule() throws Exception {
    when(createCarreraTaxiUseCase.execute(any()))
        .thenThrow(InvalidCarreraTaxiTaxiException.becauseFormatIsInvalid("12AB"));

    mockMvc
        .perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message")
                .value("La placa del taxi '12AB' no es válida. Formato esperado: ABC123."));
  }

  // ── actualizar

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("PUT /api/carreras-taxi/{id} actualiza la carrera con el id de la ruta")
  void shouldUpdateCarrera() throws Exception {
    final UpdateCarreraTaxiCommand expectedCommand =
        new UpdateCarreraTaxiCommand(
            ID,
            "María Fernanda Pájaro",
            "UAK512",
            new BigDecimal("6.4"),
            "El Recreo",
            "Centro Histórico",
            2,
            "Jorge Luis Marrugo",
            new BigDecimal("14000"),
            25);
    when(updateCarreraTaxiUseCase.execute(expectedCommand)).thenReturn(sampleCarrera());

    mockMvc
        .perform(
            put(BASE_PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID));

    verify(updateCarreraTaxiUseCase).execute(expectedCommand);
  }

  // ── eliminar

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("DELETE /api/carreras-taxi/{id} elimina la carrera y responde 204")
  void shouldDeleteCarrera() throws Exception {
    mockMvc.perform(delete(BASE_PATH + "/" + ID)).andExpect(status().isNoContent());

    verify(deleteCarreraTaxiUseCase).execute(new DeleteCarreraTaxiCommand(ID));
  }

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("DELETE /api/carreras-taxi/{id} con rol MEMBER responde 403")
  void shouldForbidDeleteForMember() throws Exception {
    mockMvc.perform(delete(BASE_PATH + "/" + ID)).andExpect(status().isForbidden());

    verify(deleteCarreraTaxiUseCase, never()).execute(any());
  }
}
