package com.cleanme.repository;

import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findUsersEntityByUid(UUID id);
    Optional<UsersEntity> findByEmail(String email);
    List<UsersEntity> findByUserType(UserType userType);
}