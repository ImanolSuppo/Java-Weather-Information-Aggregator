package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.controllers.UserController;
import ar.edu.utn.frc.tup.lciii.dtos.common.UserRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.UserResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.WeatherResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.services.LocationService;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import ar.edu.utn.frc.tup.lciii.services.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private LocationService locationService;

    @Mock
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void login() {
        // Crear un usuario de prueba
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("testemail");
        userRequestDTO.setTemperature_unit("testunit");

        // Mockear el servicio de usuario para devolver una respuesta simulada
        UserResponseDTO expectedResponse = new UserResponseDTO();
        expectedResponse.setClient_id(1L);
        when(userService.login(userRequestDTO)).thenReturn(expectedResponse);

        // Llamar al método que deseas probar
        ResponseEntity<UserResponseDTO> response = userController.login(userRequestDTO);

        // Verificar que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void getLocations() {
        // Mockear el servicio de validación para que siempre retorne verdadero
        when(validationService.validation(any())).thenReturn(true);

        // Crear parámetros de solicitud simulados
        Long clientId = 1L;
        UUID clientSecret = UUID.randomUUID();

        // Mockear el servicio de ubicación para devolver una respuesta simulada
        Location[] expectedLocations = new Location[]{new Location(1L, "Location 1", "", "")};
        ResponseEntity<Location[]> expectedResponse = new ResponseEntity<>(expectedLocations, HttpStatus.OK);
        when(locationService.getLocations()).thenReturn(expectedResponse);

        // Llamar al método que deseas probar
        ResponseEntity<Location[]> response = userController.getLocations(clientId, clientSecret);

        // Verificar que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(expectedLocations, response.getBody());
    }

    @Test
    void getWeatherByLocation() {
        // Mockear el servicio de validación para que siempre retorne verdadero
        when(validationService.validation(any())).thenReturn(true);

        // Crear parámetros de solicitud simulados
        Long locationId = 1L;
        String date = "2023-10-03T12:00:00Z";
        Long clientId = 1L;
        UUID clientSecret = UUID.randomUUID();

        // Mockear el servicio de ubicación para devolver una respuesta simulada
        WeatherResponseDTO expectedResponse = new WeatherResponseDTO();
        // Configura aquí los datos esperados en el objeto expectedResponse

        when(locationService.getWeather(any())).thenReturn(expectedResponse);

        // Llamar al método que deseas probar
        ResponseEntity<WeatherResponseDTO> response = userController.getWeatherByLocation(locationId, date, clientId, clientSecret);

        // Verificar que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }


}