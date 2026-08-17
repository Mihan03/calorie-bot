package com.caloriebot.userservice.mapper;

import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {

    @Test
    void shouldMapUserToUserDtoResponse() {
        UserMapper userMapper = new UserMapperImpl();
        User inputUser = new User(UUID.randomUUID(), 1L);

        UserDtoResponse userDtoResponse = userMapper.toUserDtoResponse(inputUser);

        assertThat(userDtoResponse.userId()).isEqualTo(inputUser.getId());
    }
}
