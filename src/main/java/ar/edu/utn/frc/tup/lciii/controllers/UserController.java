package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.UserRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.UserResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.ValidationRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.WeatherResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.services.LocationService;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import ar.edu.utn.frc.tup.lciii.services.ValidationService;
import io.swagger.v3.oas.annotations.headers.Header;
import org.modelmapper.ValidationException;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;
    @Autowired
    private ValidationService validationService;
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok(userService.login(userRequestDTO));
    }

    @GetMapping("/location")
    public ResponseEntity<Location[]> getLocations(@RequestHeader("client_id") Long client_id,
                                                   @RequestHeader("client_secret")UUID secret){
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO();
        validationRequestDTO.setSecret(secret);
        validationRequestDTO.setClient_id(client_id);
        if(validationService.validation(validationRequestDTO)){
            return ResponseEntity.ok(locationService.getLocations().getBody());
        }
        throw new RuntimeException("User not found");
    }
    @GetMapping("/location/{id}")
    public ResponseEntity<WeatherResponseDTO> getWeatherByLocation(@PathVariable Long id,
                                                                   @RequestParam(name = "datetime") String date,
                                                                   @RequestHeader("client_id") Long client_id,
                                                                   @RequestHeader("client_secret")UUID secret){
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO();
        validationRequestDTO.setLocation_id(id);
        validationRequestDTO.setSecret(secret);
        validationRequestDTO.setClient_id(client_id);
        validationRequestDTO.setDate(date);
        if(validationService.validation(validationRequestDTO)){
            return ResponseEntity.ok(locationService.getWeather(validationRequestDTO));
        }
        throw new RuntimeException("User not found");
    }
}
