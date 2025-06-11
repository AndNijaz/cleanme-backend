package com.cleanme.repository;

import com.cleanme.entity.UsersEntity;
import com.cleanme.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findUsersEntityByUid(UUID id);
    Optional<UsersEntity> findByEmail(String email);
    List<UsersEntity> findByUserType(UserType userType);
    
    // OPTIMIZED: Get all cleaners with their details (LEFT JOIN to include cleaners without setup)
    @Query("SELECT u FROM UsersEntity u LEFT JOIN FETCH u.cleanerDetails WHERE u.userType = 'CLEANER'")
    List<UsersEntity> findAllCleanersWithDetails();
}