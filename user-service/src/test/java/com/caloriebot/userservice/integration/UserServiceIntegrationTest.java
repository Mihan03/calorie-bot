package com.caloriebot.userservice.integration;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.entity.UserStateEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.repository.UserRepository;
import com.caloriebot.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

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

        UserEntity existingUser = new UserEntity();
        existingUser.setTgId(TG_ID);

        UserStateEntity userStateEntity = new UserStateEntity();
        userStateEntity.setState(UserState.WAITING_WEIGHT);
        userStateEntity.setUser(existingUser);

        existingUser.setUserState(userStateEntity);

        existingUser = userRepository.save(existingUser);

        UserDtoResponse response = userService.processingStart(request);

        assertThat(response.userId()).isEqualTo(existingUser.getId());
        assertThat(response.userState()).isEqualTo(UserState.WAITING_WEIGHT.name());
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldChangeStateWhenUserStateIsNew() {
        UserEntity existingUser = new UserEntity();
        existingUser.setTgId(TG_ID);

        UserStateEntity userStateEntity = new UserStateEntity();
        userStateEntity.setState(UserState.NEW);
        userStateEntity.setUser(existingUser);

        existingUser.setUserState(userStateEntity);

        userRepository.save(existingUser);

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
        UserEntity existingUser = new UserEntity();
        existingUser.setTgId(TG_ID);

        UserStateEntity userStateEntity = new UserStateEntity();
        userStateEntity.setState(UserState.WAITING_HEIGHT);
        userStateEntity.setUser(existingUser);

        existingUser.setUserState(userStateEntity);

        userRepository.save(existingUser);

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

    @Test
    void shouldRestartOnboardingFromWaitingHeight() {
        UserEntity existingUser = new UserEntity();
        existingUser.setTgId(TG_ID);

        UserStateEntity userStateEntity = new UserStateEntity();
        userStateEntity.setState(UserState.WAITING_HEIGHT);
        userStateEntity.setUser(existingUser);

        existingUser.setUserState(userStateEntity);

        userRepository.save(existingUser);

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
        UserEntity existingUser = new UserEntity();
        existingUser.setTgId(TG_ID);

        UserStateEntity userStateEntity = new UserStateEntity();
        userStateEntity.setState(UserState.NEW);
        userStateEntity.setUser(existingUser);

        existingUser.setUserState(userStateEntity);

        userRepository.save(existingUser);

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
