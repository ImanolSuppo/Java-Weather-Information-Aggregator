package ar.edu.utn.frc.tup.lciii.RestClients;

import ar.edu.utn.frc.tup.lciii.models.*;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class ApiRestClient {
    RestTemplate restTemplate = new RestTemplate();

    String baseResourceUrlLocation = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/location";
    String baseResourceUrlTemperature = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/temperature";
    String baseResourceUrlWind = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/wind";
    String baseResourceUrlAirQuality = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/air_quality";
    String baseResourceUrlCloudiness = "https://my-json-server.typicode.com/LCIV-2023/fake-weather/cloudiness";

    public ResponseEntity<Location[]> getPosts() {
        return restTemplate.getForEntity(baseResourceUrlLocation, Location[].class);
    }

    public ResponseEntity<Temperature[]> getPostsTemperature(){
        return restTemplate.getForEntity(baseResourceUrlTemperature, Temperature[].class);
    }

    public ResponseEntity<Wind[]> getPostsWind() {
        return restTemplate.getForEntity(baseResourceUrlWind, Wind[].class);
    }

    public ResponseEntity<AirQuality[]> getPostsAirQuality() {
        return restTemplate.getForEntity(baseResourceUrlAirQuality, AirQuality[].class);
    }

    public ResponseEntity<Cloudiness[]> getPostsCloudiness() {
        return restTemplate.getForEntity(baseResourceUrlCloudiness, Cloudiness[].class);
    }
}
