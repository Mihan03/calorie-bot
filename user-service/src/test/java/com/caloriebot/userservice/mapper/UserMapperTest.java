package com.caloriebot.userservice.mapper;

import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.model.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {

    @Test
    void shouldMapUserToUserDtoResponse() {
        UserMapper userMapper = new UserMapperImpl();
        UserEntity inputUserEntity = new UserEntity();
        inputUserEntity.setId(UUID.randomUUID());
        inputUserEntity.setTgId(1L);

        UserDtoResponse userDtoResponse = userMapper.toUserDtoResponse(inputUserEntity);

        assertThat(userDtoResponse.userId()).isEqualTo(inputUserEntity.getId());
    }
}
