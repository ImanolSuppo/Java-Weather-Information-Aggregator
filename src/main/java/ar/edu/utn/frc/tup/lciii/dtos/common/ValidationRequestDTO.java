package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.UUID;

@Data
public class ValidationRequestDTO {
    private Long location_id;
    private String date;
    private Long client_id;
    private UUID secret;
}
