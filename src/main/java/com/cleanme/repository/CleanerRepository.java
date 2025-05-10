package com.cleanme.repository;

import com.cleanme.entity.CleanerDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CleanerRepository extends JpaRepository<CleanerDetailsEntity, UUID> {
    boolean existsByCleaner_Uid(UUID cleanerId);
}
