package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.UserRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.UserResponseDTO;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
@Service
public class UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserResponseDTO login(UserRequestDTO userRequestDTO){
        UUID secret = UUID.randomUUID();
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setUnit(userRequestDTO.getTemperature_unit());
        user.setSecret(secret);
        user.setCount(0L);
        UserEntity userSaved = userJpaRepository.save(modelMapper.map(user, UserEntity.class));
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setClient_id(userSaved.getId());
        userResponseDTO.setSecret(userSaved.getSecret());
        return userResponseDTO;
    }

    public User getUserById(Long id){
        return modelMapper.map(userJpaRepository.getReferenceById(id), User.class);
    }

    public void postCount(Long clientId) {
        User user = getUserById(clientId);
        user.setCount(user.getCount() + 1);
        userJpaRepository.save(modelMapper.map(user, UserEntity.class));
    }
}
