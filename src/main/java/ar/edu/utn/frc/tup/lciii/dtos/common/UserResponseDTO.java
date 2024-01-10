package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDTO {

    private Long client_id;
    private UUID secret;
}
