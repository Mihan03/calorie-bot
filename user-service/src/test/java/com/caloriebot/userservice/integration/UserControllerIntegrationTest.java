package com.caloriebot.userservice.integration;

import com.caloriebot.userservice.dto.*;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.entity.UserStateEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
public class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private UserRepository userRepository;

    private static final String BASE_URL = "/api/v1/users";

    @Test
    void shouldReturnOkWhenProcessingStart() {
        restTestClient.post()
                .uri(BASE_URL + "/processing-start")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserDtoRequest(TG_ID))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDtoResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.userId()).isNotNull();
                    assertThat(response.userState()).isEqualTo(UserState.NEW.name());
                });
    }

    @Test
    void shouldReturnOkWhenStartingConfigure() {
        createTestUser(UserState.NEW);

        restTestClient.post()
                .uri(BASE_URL + "/by-telegram/{tgId}/onboarding/start-configure", TG_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StartConfigureResponseDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.userState()).isEqualTo(UserState.WAITING_WEIGHT);
                    assertThat(response.applied()).isTrue();
                });
    }

    @Test
    void shouldReturnConflictWhenStartingConfigure() {
        createTestUser(UserState.WAITING_HEIGHT);

        restTestClient.post()
                .uri(BASE_URL + "/by-telegram/{tgId}/onboarding/start-configure", TG_ID)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(StartConfigureResponseDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.userState()).isEqualTo(UserState.WAITING_HEIGHT);
                    assertThat(response.applied()).isFalse();
                });
    }

    @Test
    void shouldReturnNotFoundWhenUserNotFound() {
        restTestClient.post()
                .uri(BASE_URL + "/by-telegram/{tgId}/onboarding/start-configure", TG_ID)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody(ApiErrorResponse.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.correlationId()).isNotBlank();
                    assertThat(response.errors()).hasSize(1);

                    ApiError error = response.errors().getFirst();

                    assertThat(error.code())
                            .isEqualTo(ErrorCode.USER_NOT_FOUND.name());
                    assertThat(error.message()).isNotBlank();
                    assertThat(error.userMessage()).isNotBlank();
                });
    }

    @Test
    void shouldReturnOkWhenRestartingConfigure() {
        createTestUser(UserState.WAITING_HEIGHT);

        restTestClient.post()
                .uri(BASE_URL + "/by-telegram/{tgId}/onboarding/restart", TG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RestartResponseDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.userState()).isEqualTo(UserState.WAITING_WEIGHT);
                    assertThat(response.applied()).isTrue();
                });
    }

    @Test
    void shouldReturnConflictWhenRestartingConfigure() {
        createTestUser(UserState.NEW);

        restTestClient.post()
                .uri(BASE_URL + "/by-telegram/{tgId}/onboarding/restart", TG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(RestartResponseDto.class)
                .value(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.userState()).isEqualTo(UserState.NEW);
                    assertThat(response.applied()).isFalse();
                });
    }

    protected void createTestUser(UserState state) {
        UserEntity user = new UserEntity();
        user.setTgId(TG_ID);

        UserStateEntity userState = new UserStateEntity();
        userState.setState(state);
        userState.setUser(user);

        user.setUserState(userState);

        userRepository.save(user);
    }
}
