package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.ErrorCode;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.enums.UserState;
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
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setTgId(1L);

        given(userRepository.findByTgId(userEntity.getTgId()))
                .willReturn(Optional.of(userEntity));

        given(userMapper.toUserDtoResponse(userEntity))
                .willReturn(new UserDtoResponse(userEntity.getId(), UserState.NEW.name()));

        UserDtoResponse userDtoResponse = userService.getUserByTgId(userEntity.getTgId());

        assertThat(userDtoResponse.userId()).isEqualTo(userEntity.getId());

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
