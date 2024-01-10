package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.RestClients.ApiRestClient;
import ar.edu.utn.frc.tup.lciii.dtos.common.ValidationRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.WeatherResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {
    private static final LocationService locationService = new LocationService();
    private static final WeatherResponseDTO weatherResponseDTO = new WeatherResponseDTO();
    private static final Long location_id = 1L;
    private static final String date = "2017-01-01T00:01:00.000Z";
    private static final User user = Mockito.mock(User.class);
    private static final UserService userService = Mockito.mock(UserService.class);

    @Test
    void getLocations() {
        locationService.setApiRestClient(new ApiRestClient());
        assertNotNull(locationService.getLocations());
    }

    @Test
    void getWeather() {
        UUID secret = UUID.randomUUID();
        locationService.setApiRestClient(new ApiRestClient());
        locationService.setUserService(userService);
        Mockito.when(userService.getUserById(1L)).thenReturn(new User(1L, "ima@gmail.com", "C", secret, 0L));
        Mockito.doNothing().when(userService).postCount(1L);
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO();
        validationRequestDTO.setDate(date);
        validationRequestDTO.setLocation_id(location_id);
        validationRequestDTO.setClient_id(1L);
        validationRequestDTO.setSecret(secret);
        assertNotNull(locationService.getWeather(validationRequestDTO));
    }

    @Test
    void isSetCloudiness() {
        locationService.setApiRestClient(new ApiRestClient());
        locationService.initializeWeather(weatherResponseDTO);
        Assertions.assertTrue(locationService.isSetCloudiness(weatherResponseDTO, location_id, date));
    }

    @Test
    void isSetAirQuality() {
        ApiRestClient restClient = Mockito.mock(ApiRestClient.class);
        locationService.setApiRestClient(restClient);
        Mockito.when(restClient.getPostsAirQuality()).thenReturn(new ResponseEntity<AirQuality[]>((AirQuality[]) null, HttpStatusCode.valueOf(500)));
        locationService.initializeWeather(weatherResponseDTO);
        Assertions.assertFalse(locationService.isSetAirQuality(weatherResponseDTO, location_id, date));
    }

    @Test
    void isSetWind() {
        ApiRestClient restClient = Mockito.mock(ApiRestClient.class);
        locationService.setApiRestClient(restClient);
        Mockito.when(restClient.getPostsWind()).thenReturn(new ResponseEntity<Wind[]>((Wind[]) null, HttpStatusCode.valueOf(500)));
        locationService.initializeWeather(weatherResponseDTO);
        Assertions.assertFalse(locationService.isSetWind(weatherResponseDTO, location_id, date));
    }

    @Test
    void isSetTemperature() {
        locationService.setApiRestClient(new ApiRestClient());
        locationService.initializeWeather(weatherResponseDTO);
        Mockito.when(user.getUnit()).thenReturn("C");
        Assertions.assertTrue(locationService.isSetTemperature(weatherResponseDTO, location_id, date, user));
    }

    @Test
    void isSetLocation() {
        ApiRestClient restClient = Mockito.mock(ApiRestClient.class);
        locationService.setApiRestClient(restClient);
        Mockito.when(restClient.getPosts()).thenReturn(new ResponseEntity<Location[]>(HttpStatusCode.valueOf(500)));
        locationService.initializeWeather(weatherResponseDTO);
        Assertions.assertFalse(locationService.isSetLocation(weatherResponseDTO, location_id));
    }

    @Test
    void getWindDirection() {
        Assertions.assertEquals("North", locationService.getWindDirection(0L));
    }
    @Test
    void getWindDirection2() {
        Assertions.assertEquals("East", locationService.getWindDirection(90L));
    }
    @Test
    void getWindDirection3() {
        Assertions.assertEquals("South", locationService.getWindDirection(180L));
    }
    @Test
    void getWindDirection4() {
        Assertions.assertEquals("West", locationService.getWindDirection(270L));
    }
    @Test
    void getAirQualityDescription() {
        Assertions.assertEquals("Good", locationService.getAirQualityDescription(10L));
    }
    @Test
    void getAirQualityDescription2() {
        Assertions.assertEquals("Moderate", locationService.getAirQualityDescription(70L));
    }
    @Test
    void getAirQualityDescription3() {
        Assertions.assertEquals("Unhealthy for Sensitive Groups", locationService.getAirQualityDescription(110L));
    }
    @Test
    void getAirQualityDescription4() {
        Assertions.assertEquals("Unhealthy", locationService.getAirQualityDescription(170L));
    }
    @Test
    void getAirQualityDescription5() {
        Assertions.assertEquals("Unknown", locationService.getAirQualityDescription(112310L));
    }


    @Test
    void getCloudinessDescription() {
        Assertions.assertEquals("Clear sky", locationService.getCloudinessDescription(2L));
    }
    @Test
    void getCloudinessDescription2() {
        Assertions.assertEquals("Few clouds", locationService.getCloudinessDescription(5L));
    }
    @Test
    void getCloudinessDescription3() {
        Assertions.assertEquals("Sky half cloudy", locationService.getCloudinessDescription(7L));
    }
    @Test
    void getCloudinessDescription4() {
        Assertions.assertEquals("Sky completely cloudy", locationService.getCloudinessDescription(12L));
    }
    @Test
    void initializeWeather() {
        WeatherResponseDTO weatherResponseDTONull = null;
        Assertions.assertThrows(NullPointerException.class, () ->
                locationService.initializeWeather(weatherResponseDTONull));
    }

    @Test
    void returnEntityNotFound() {
        Long locationId = 1L;
        String date = "2023-10-03T12:00:00.000Z";
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            locationService.returnEntityNotFound(locationId, date);
        });
    }
    @Test
    void returnEntityNotFoundMessage() {
        Long locationId = 1L;
        String date = "2023-10-03T12:00:00.000Z";
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationService.returnEntityNotFound(locationId, date);
        });
        String expectedMessage = "The location id 1 or date 2023-10-03T12:00:00.000Z not found";
        assertEquals(expectedMessage, exception.getMessage());
    }
}