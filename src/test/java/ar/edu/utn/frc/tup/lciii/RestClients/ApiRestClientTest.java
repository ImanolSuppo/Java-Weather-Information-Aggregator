package ar.edu.utn.frc.tup.lciii.RestClients;

import ar.edu.utn.frc.tup.lciii.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RestClientTest(ApiRestClientTest.class)
class ApiRestClientTest {


    private ApiRestClient apiRestClient;
    @Mock
    private RestTemplate restTemplate;

    String baseResourceUrlLocation = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/location";
    String baseResourceUrlTemperature = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/temperature";
    String baseResourceUrlWind = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/wind";
    String baseResourceUrlAirQuality = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/air_quality";
    String baseResourceUrlCloudiness = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/cloudiness";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        apiRestClient = new ApiRestClient();
        apiRestClient.setRestTemplate(restTemplate);
    }

    @Test
    void getPosts() {
        Location[] expectedLocations = new Location[]{new Location(1L, "Location 1", "", "")};
        ResponseEntity<Location[]> responseEntity = new ResponseEntity<>(expectedLocations, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity(baseResourceUrlLocation, Location[].class)).thenReturn(responseEntity);
        ResponseEntity<Location[]> result = apiRestClient.getPosts();
        verify(restTemplate, times(1)).getForEntity(baseResourceUrlLocation, Location[].class);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertArrayEquals(expectedLocations, result.getBody());
    }

    @Test
    void getPostsTemperature() {
        Temperature[] expected = new Temperature[]{new Temperature(1L, 1L, 33L, "C", "")};
        ResponseEntity<Temperature[]> responseEntity = new ResponseEntity<>(expected, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity(baseResourceUrlTemperature, Temperature[].class)).thenReturn(responseEntity);
        ResponseEntity<Temperature[]> result = apiRestClient.getPostsTemperature();
        verify(restTemplate, times(1)).getForEntity(baseResourceUrlTemperature, Temperature[].class);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertArrayEquals(expected, result.getBody());
    }

    @Test
    void getPostsWind() {
        Wind[] expected = new Wind[]{new Wind(1L, 1L, 33L, 90L, "")};
        ResponseEntity<Wind[]> responseEntity = new ResponseEntity<>(expected, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity(baseResourceUrlWind, Wind[].class)).thenReturn(responseEntity);
        ResponseEntity<Wind[]> result = apiRestClient.getPostsWind();
        verify(restTemplate, times(1)).getForEntity(baseResourceUrlWind, Wind[].class);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertArrayEquals(expected, result.getBody());
    }

    @Test
    void getPostsAirQuality() {
        AirQuality[] expected = new AirQuality[]{new AirQuality(1L, 33L, 90L, "")};
        ResponseEntity<AirQuality[]> responseEntity = new ResponseEntity<>(expected, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity(baseResourceUrlAirQuality, AirQuality[].class)).thenReturn(responseEntity);
        ResponseEntity<AirQuality[]> result = apiRestClient.getPostsAirQuality();
        verify(restTemplate, times(1)).getForEntity(baseResourceUrlAirQuality, AirQuality[].class);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertArrayEquals(expected, result.getBody());
    }

    @Test
    void getPostsCloudiness() {
        Cloudiness[] expected = new Cloudiness[]{new Cloudiness(1L, 33L, 90L, "")};
        ResponseEntity<Cloudiness[]> responseEntity = new ResponseEntity<>(expected, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity(baseResourceUrlCloudiness, Cloudiness[].class)).thenReturn(responseEntity);
        ResponseEntity<Cloudiness[]> result = apiRestClient.getPostsCloudiness();
        verify(restTemplate, times(1)).getForEntity(baseResourceUrlCloudiness, Cloudiness[].class);
        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertArrayEquals(expected, result.getBody());
    }


}