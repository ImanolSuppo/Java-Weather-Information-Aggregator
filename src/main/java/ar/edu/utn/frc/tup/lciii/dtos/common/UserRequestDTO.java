package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String email;
    private String temperature_unit;
}
