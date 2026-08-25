package com.caloriebot.userservice.repository;

import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.enums.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByTgId(Long tgId);

    @Modifying
    @Query("update UserStateEntity s set s.state = :to where s.user = :user and s.state = :from")
    int changeState(UserEntity user, UserState from, UserState to);
}
