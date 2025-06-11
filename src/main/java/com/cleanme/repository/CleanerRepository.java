package com.cleanme.repository;

import com.cleanme.entity.CleanerDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CleanerRepository extends JpaRepository<CleanerDetailsEntity, UUID> {
    boolean existsByCleaner_Uid(UUID cleanerId);
    Optional<CleanerDetailsEntity> findByCleaner_Uid(UUID cleanerId);
    
    // Optimized queries to eliminate N+1 problems
    @Query("SELECT cd FROM CleanerDetailsEntity cd JOIN FETCH cd.cleaner u WHERE u.userType = 'CLEANER'")
    List<CleanerDetailsEntity> findAllCleanersWithDetails();
    
    @Query("SELECT cd FROM CleanerDetailsEntity cd JOIN FETCH cd.cleaner u WHERE cd.cleaner.uid = :cleanerId")
    Optional<CleanerDetailsEntity> findByCleanerUidWithUser(@Param("cleanerId") UUID cleanerId);
    
    @Query("SELECT cd FROM CleanerDetailsEntity cd JOIN FETCH cd.cleaner u WHERE u.uid IN :cleanerIds")
    List<CleanerDetailsEntity> findByCleanerUidsWithUsers(@Param("cleanerIds") List<UUID> cleanerIds);
}