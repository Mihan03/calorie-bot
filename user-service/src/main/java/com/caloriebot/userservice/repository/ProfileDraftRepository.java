package com.caloriebot.userservice.repository;

import com.caloriebot.userservice.model.entity.ProfileDraftEntity;
import com.caloriebot.userservice.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileDraftRepository extends JpaRepository<ProfileDraftEntity, UUID> {
    Optional<ProfileDraftEntity> findByUser(UserEntity user);
}
