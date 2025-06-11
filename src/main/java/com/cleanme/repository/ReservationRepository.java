package com.cleanme.repository;


import com.cleanme.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
    List<ReservationEntity> findByUser_Uid(UUID id);
    Optional<ReservationEntity> findReservationEntityByRid(UUID id);
    void deleteByRid(UUID id);
    List<ReservationEntity> findByCleaner_Uid(UUID id);
    
    // Optimized queries to eliminate N+1 problems
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.user JOIN FETCH r.cleaner WHERE r.user.uid = :userId")
    List<ReservationEntity> findByUserUidWithDetails(@Param("userId") UUID userId);
    
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.user JOIN FETCH r.cleaner WHERE r.cleaner.uid = :cleanerId")
    List<ReservationEntity> findByCleanerUidWithDetails(@Param("cleanerId") UUID cleanerId);
    
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.user JOIN FETCH r.cleaner WHERE r.rid = :rid")
    Optional<ReservationEntity> findByRidWithDetails(@Param("rid") UUID rid);
    
    @Query("SELECT r FROM ReservationEntity r JOIN FETCH r.cleaner WHERE r.cleaner.uid = :cleanerId AND r.date = :date")
    List<ReservationEntity> findByCleanerUidAndDateWithDetails(@Param("cleanerId") UUID cleanerId, @Param("date") LocalDate date);
}