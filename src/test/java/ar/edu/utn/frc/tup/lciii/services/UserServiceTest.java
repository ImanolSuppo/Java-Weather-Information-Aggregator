package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.UserRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.UserResponseDTO;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final UserService userService = new UserService();
    private static final UserJpaRepository userJpa = Mockito.mock(UserJpaRepository.class);
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final UUID secret = UUID.randomUUID();
    private static final User user = new User(1L, "ima@gmail", "C", secret, 0L);

    @BeforeAll
    public static void setUp(){
        userService.setUserJpaRepository(userJpa);
        userService.setModelMapper(modelMapper);
    }
    @Test
    void login() {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        Mockito.when(userJpa.save(userEntity)).thenReturn(userEntity);
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("ima@gmail");
        userRequestDTO.setTemperature_unit("C");
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setClient_id(user.getId());
        userResponseDTO.setSecret(secret);
    }

    @Test
    void getUserById() {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        Mockito.when(userJpa.getReferenceById(1L)).thenReturn(userEntity);
        assertEquals(user, userService.getUserById(1L));
    }

    @Test
    void postCount() {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setCount(user.getCount() + 1);
        Mockito.when(userJpa.save(userEntity)).thenReturn(userEntity);
        Mockito.when(userJpa.getReferenceById(1L)).thenReturn(userEntity);
        userService.postCount(user.getId());
        assertEquals(1L, userService.getUserById(1L).getCount());
    }
}