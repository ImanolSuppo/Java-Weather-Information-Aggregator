package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.RestClients.ApiRestClient;
import ar.edu.utn.frc.tup.lciii.dtos.common.ValidationRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.WeatherResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Data
@Service
public class LocationService {
    @Autowired
    private ApiRestClient apiRestClient;
    @Autowired
    private UserService userService;

    public ResponseEntity<Location[]> getLocations(){
        return apiRestClient.getPosts();
    }

    public WeatherResponseDTO getWeather(ValidationRequestDTO validationRequestDTO){
        WeatherResponseDTO weatherResponseDTO = new WeatherResponseDTO();
        initializeWeather(weatherResponseDTO);
        User user = userService.getUserById(validationRequestDTO.getClient_id());
        Long location_id = validationRequestDTO.getLocation_id();
        String date = validationRequestDTO.getDate();
        if (!isSetLocation(weatherResponseDTO, location_id)) {
            returnErrorServer();        }
        if(!isSetTemperature(weatherResponseDTO, location_id, date , user)){
            returnErrorServer();        }
        if(!isSetWind(weatherResponseDTO , location_id, date)){
            returnErrorServer();        }
        if(!isSetAirQuality(weatherResponseDTO, location_id, date)){
            returnErrorServer();        }
        if(!isSetCloudiness(weatherResponseDTO, location_id, date)){
            returnErrorServer();        }
        userService.postCount(validationRequestDTO.getClient_id());
        return weatherResponseDTO;
    }

    public boolean isSetCloudiness(WeatherResponseDTO weatherResponseDTO, Long locationId, String date) {
        ResponseEntity<Cloudiness[]> cloudiness = apiRestClient.getPostsCloudiness();
        if(cloudiness.getStatusCode().is2xxSuccessful() && cloudiness.getBody() != null){
            for (Cloudiness cloud: cloudiness.getBody()) {
                if(cloud.location_id().equals(locationId) && cloud.created_at().equals(date)){
                    weatherResponseDTO.getCloudiness().setIndex(cloud.cloudiness());
                    weatherResponseDTO.getCloudiness().setDescription(getCloudinessDescription(cloud.cloudiness()));
                    return true;
                }
            }
            returnEntityNotFound(locationId, date);
        }
        return false;
    }

    public boolean isSetAirQuality(WeatherResponseDTO weatherResponseDTO, Long locationId, String date) {
        ResponseEntity<AirQuality[]> airQualities = apiRestClient.getPostsAirQuality();
        if(airQualities.getStatusCode().is2xxSuccessful() && airQualities.getBody() != null){
            for (AirQuality air: airQualities.getBody()) {
                if(air.location_id().equals(locationId) && air.created_at().equals(date)){
                    weatherResponseDTO.getAirQuality().setIndex(air.quality());
                    weatherResponseDTO.getAirQuality().setDescription(getAirQualityDescription(air.quality()));
                    return true;
                }
            }
            returnEntityNotFound(locationId, date);
        }
        return false;
    }

    public boolean isSetWind(WeatherResponseDTO weatherResponseDTO, Long locationId, String date) {
        ResponseEntity<Wind[]> winds = apiRestClient.getPostsWind();
        if(winds.getStatusCode().is2xxSuccessful() && winds.getBody() != null){
            for (Wind wind: winds.getBody()) {
                if(wind.location_id().equals(locationId) && wind.created_at().equals(date)){
                    weatherResponseDTO.setWind(wind.speed() + " Km/h from " + getWindDirection(wind.direction()));
                    return true;
                }
            }
            returnEntityNotFound(locationId, date);
        }
        return false;
    }

    public boolean isSetTemperature(WeatherResponseDTO weatherResponseDTO, Long locationId, String date , User user) {
        ResponseEntity<Temperature[]> temperatures = apiRestClient.getPostsTemperature();
        if(temperatures.getStatusCode().is2xxSuccessful() && temperatures.getBody() != null){
            for (Temperature temperature: temperatures.getBody()) {
                if(temperature.location_id().equals(locationId) && temperature.created_at().equals(date)){
                    String unit = (user.getUnit().equals("C")) ? "C" : "F";
                    Long value = (user.getUnit().equals("C")) ? temperature.temperature() : temperature.temperature() * 33;
                    weatherResponseDTO.getTemperature().setUnit(unit);
                    weatherResponseDTO.getTemperature().setValue(value);
                    return true;
                }
            }
            returnEntityNotFound(locationId, date);
        }
        return false;
    }

    public boolean isSetLocation(WeatherResponseDTO weatherResponseDTO, Long location_id) {
        ResponseEntity<Location[]> locations = apiRestClient.getPosts();
        if(locations.getStatusCode().is2xxSuccessful() && locations.getBody() != null){
            Optional<Location> location = Arrays.stream(locations.getBody())
                    .filter(x -> x.id().equals(location_id)).findFirst();
            if(location.isPresent()){
                weatherResponseDTO.getLocation().setId(location.get().id());
                weatherResponseDTO.getLocation().setName(location.get().name());
                return true;
            }
            else{
                throw new EntityNotFoundException(String.format("The location id %s not found", location_id));
            }
        }
        return false;
    }
    public String getWindDirection(Long direction) {
        if (direction == 0) {
            return "North";
        } else if (direction == 40) {
            return "North-East";
        } else if (direction == 90) {
            return "East";
        } else if (direction == 180) {
            return "South";
        } else if (direction == 270) {
            return "West";
        }
        return "Unknown";
    }
    public String getAirQualityDescription(Long index) {
        if (index >= 0 && index <= 50) {
            return "Good";
        } else if (index >= 51 && index <= 100) {
            return "Moderate";
        } else if (index >= 101 && index <= 150) {
            return "Unhealthy for Sensitive Groups";
        } else if (index >= 151 && index <= 200) {
            return "Unhealthy";
        } else if (index >= 201 && index <= 300) {
            return "Very Unhealthy";
        } else if (index >= 301 && index <= 500) {
            return "Hazardous";
        } else {
            return "Unknown";
        }
    }
    public String getCloudinessDescription(Long index) {
        if (index >= 0 && index <= 3) {
            return "Clear sky";
        } else if (index >= 4 && index <= 6) {
            return "Few clouds";
        } else if (index >= 7 && index <= 8) {
            return "Sky half cloudy";
        } else {
            return "Sky completely cloudy";
        }
    }

    public void initializeWeather(WeatherResponseDTO weatherResponseDTO) {
        weatherResponseDTO.setLocation(new WeatherResponseDTO.Location());
        weatherResponseDTO.setCloudiness(new WeatherResponseDTO.Cloudiness());
        weatherResponseDTO.setTemperature(new WeatherResponseDTO.Temperature());
        weatherResponseDTO.setAirQuality(new WeatherResponseDTO.AirQuality());
    }

    public void returnEntityNotFound(Long locationId, String date){
        throw new EntityNotFoundException(String.format("The location id %s or date %s not found", locationId, date));
    }

    private void returnErrorServer(){
        throw new ResponseStatusException(HttpStatusCode.valueOf(500),
                "El servidor no está disponible en este momento.");
    }

}
