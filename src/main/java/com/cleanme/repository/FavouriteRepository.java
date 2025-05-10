package com.cleanme.repository;

import com.cleanme.entity.FavouriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<FavouriteEntity, UUID> {
    List<FavouriteEntity> findByClient_Uid(UUID clientId);

    boolean existsByClient_UidAndCleaner_Uid(UUID clientId, UUID cleanerId);

    void deleteByClient_UidAndCleaner_Uid(UUID clientId, UUID cleanerId);
}
