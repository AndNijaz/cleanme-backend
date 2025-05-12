package com.cleanme.repository;

import com.cleanme.entity.ReviewEntity;
import com.cleanme.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findByCleaner(UsersEntity cleaner);
}
