package com.cleanme.repository;

import com.cleanme.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    
}
