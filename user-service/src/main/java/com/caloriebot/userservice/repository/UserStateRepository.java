package com.caloriebot.userservice.repository;

import com.caloriebot.userservice.model.entity.UserStateEntity;
import com.caloriebot.userservice.model.enums.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface UserStateRepository extends JpaRepository<UserStateEntity, UUID> {
    @Modifying
    @Query("update UserStateEntity s set s.state = :to where s.user.tgId = :tgId and s.state = :from")
    int changeState(Long tgId, UserState from, UserState to);

    @Modifying
    @Query("update UserStateEntity s set s.state = :to where s.user.tgId = :tgId and s.state IN :from")
    int restartOnboarding(Long tgId, Collection<UserState> from, UserState to);
}
