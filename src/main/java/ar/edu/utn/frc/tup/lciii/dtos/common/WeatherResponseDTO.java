package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class WeatherResponseDTO {
    private Location location;
    private Temperature temperature;
    private String wind;
    private AirQuality airQuality;
    private Cloudiness cloudiness;

    @Data
    public static class Location {
        private Long id;
        private String name;
    }

    @Data
    public static class Temperature {
        private Long value;
        private String unit;
    }

    @Data
    public static class AirQuality {
        private Long index;
        private String description;
    }

    @Data
    public static class Cloudiness {
        private Long index;
        private String description;
    }
}
