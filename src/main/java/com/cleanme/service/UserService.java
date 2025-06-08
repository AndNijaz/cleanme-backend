package com.cleanme.service;

import com.cleanme.dto.UserDto;
import com.cleanme.entity.UsersEntity;
import com.cleanme.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    public UserDto getUserById(UUID userId) {
        UsersEntity user = usersRepository.findUsersEntityByUid(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    public UserDto updateUser(UUID userId, UserDto userDto) {
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());

        UsersEntity updatedUser = usersRepository.save(user);
        return mapToDto(updatedUser);
    }

    private UserDto mapToDto(UsersEntity user) {
        UserDto dto = new UserDto();
        dto.setId(user.getUid());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        // ... map other fields as needed
        return dto;
    }
} 