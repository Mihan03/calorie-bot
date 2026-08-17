package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.ErrorCode;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.User;
import com.caloriebot.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUserDtoWhenUserExists() {
        Long tgId = 1L;
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, tgId);

        given(userRepository.findByTgId(tgId))
                .willReturn(Optional.of(user));

        given(userMapper.toUserDtoResponse(user))
                .willReturn(new UserDtoResponse(uuid));

        UserDtoResponse userDtoResponse = userService.getUserByTgId(tgId);

        assertThat(userDtoResponse.userId()).isEqualTo(uuid);

    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        Long tgId = 1L;

        given(userRepository.findByTgId(tgId)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserByTgId(tgId));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

        verifyNoInteractions(userMapper);
    }
}
