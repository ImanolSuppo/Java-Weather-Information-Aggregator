package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.ValidationRequestDTO;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    private static final ValidationService validationService = new ValidationService();
    private static final UserJpaRepository userJpa = Mockito.mock(UserJpaRepository.class);
    private static final UUID secret = UUID.randomUUID();

    @Test
    void validation() {
        validationService.setUserJpaRepository(userJpa);
        ValidationRequestDTO validationRequestDTO = new ValidationRequestDTO();
        validationRequestDTO.setSecret(secret);
        validationRequestDTO.setClient_id(1L);
        Mockito.when(userJpa.findBySecret(secret)).thenReturn(Optional.empty());
        Assertions.assertFalse(validationService.validation(validationRequestDTO));
    }
}