package com.cleanme.repository;


import com.cleanme.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

    List<ReservationEntity> findByUser_Uid(UUID id);

    Optional<ReservationEntity> findReservationEntityByRid(UUID id);

    void deleteByRid(UUID id);

}
