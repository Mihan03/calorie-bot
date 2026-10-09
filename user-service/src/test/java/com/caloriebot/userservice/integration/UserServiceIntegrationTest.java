package com.caloriebot.userservice.integration;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    UserService userService;

    private static final Long UNKNOWN_TG_ID = 2L;

    @Test
    void shouldCreateAndReturnUserWhenUserDoesNotExist() {
        UserDtoRequest request = new UserDtoRequest(TG_ID);

        UserDtoResponse response = userService.processingStart(request);

        assertThat(response).isNotNull();
        assertThat(response.userId()).isNotNull();
        assertThat(response.userState()).isEqualTo(UserState.NEW.name());

        UserEntity savedUser = userRepository.findByTgId(TG_ID).orElseThrow();

        assertThat(savedUser.getTgId()).isEqualTo(TG_ID);
        assertThat(savedUser.getUserState()).isNotNull();
        assertThat(savedUser.getUserState().getState()).isEqualTo(UserState.NEW);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldReturnExistingUserWithoutChangingState() {
        UserDtoRequest request = new UserDtoRequest(TG_ID);

        UserEntity existingUser = createTestUser(UserState.WAITING_WEIGHT);

        UserDtoResponse response = userService.processingStart(request);

        assertThat(response.userId()).isEqualTo(existingUser.getId());
        assertThat(response.userState()).isEqualTo(UserState.WAITING_WEIGHT.name());
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldChangeStateWhenUserStateIsNew() {
        createTestUser(UserState.NEW);

        StartConfigureResponseDto response = userService.processingStateStartConfigure(TG_ID);

        assertThat(response.userState()).isEqualTo(UserState.WAITING_WEIGHT);
        assertThat(response.applied()).isTrue();

        UserEntity actualUser = userRepository.findByTgId(TG_ID)
                .orElseThrow();

        assertThat(actualUser.getUserState().getState())
                .isEqualTo(UserState.WAITING_WEIGHT);
    }

    @Test
    void shouldNotChangeStateWhenUserStateDoesNotNew() {
        createTestUser(UserState.WAITING_HEIGHT);

        StartConfigureResponseDto response = userService.processingStateStartConfigure(TG_ID);
        assertThat(response.userState()).isEqualTo(UserState.WAITING_HEIGHT);
        assertThat(response.applied()).isFalse();

        UserEntity actualUser = userRepository.findByTgId(TG_ID)
                .orElseThrow();

        assertThat(actualUser.getUserState().getState())
                .isEqualTo(UserState.WAITING_HEIGHT);
    }

    @Test
    void shouldThrowNotFoundWhenStartingConfigureForUnknownUser() {
        assertThatThrownBy(
                () -> userService.processingStateStartConfigure(UNKNOWN_TG_ID)
        ).isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @EnumSource(names = {"WAITING_WEIGHT", "WAITING_HEIGHT"})
    void shouldRestartOnboardingFromAnyOnboardingState(UserState initial) {
        createTestUser(initial);

        RestartResponseDto restartResponseDto = userService.restartOnboarding(TG_ID);
        assertThat(restartResponseDto.userState()).isEqualTo(UserState.WAITING_WEIGHT);
        assertThat(restartResponseDto.applied()).isTrue();

        UserEntity actualUser = userRepository.findByTgId(TG_ID)
                .orElseThrow();

        assertThat(actualUser.getUserState().getState())
                .isEqualTo(UserState.WAITING_WEIGHT);
    }

    @Test
    void shouldNotRestartOnboardingFromNew() {
        createTestUser(UserState.NEW);

        RestartResponseDto restartResponseDto = userService.restartOnboarding(TG_ID);
        assertThat(restartResponseDto.userState()).isEqualTo(UserState.NEW);
        assertThat(restartResponseDto.applied()).isFalse();

        UserEntity actualUser = userRepository.findByTgId(TG_ID)
                .orElseThrow();

        assertThat(actualUser.getUserState().getState())
                .isEqualTo(UserState.NEW);
    }

    @Test
    void shouldThrowNotFoundWhenRestartingUnknownUserOnboarding() {
        assertThatThrownBy(
                () -> userService.restartOnboarding(UNKNOWN_TG_ID)
        ).isInstanceOf(NotFoundException.class);
    }
}
