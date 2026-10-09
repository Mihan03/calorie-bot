package com.caloriebot.gateway.client;

import com.caloriebot.gateway.UserState;
import com.caloriebot.gateway.client.configuration.RestClientConfig;
import com.caloriebot.gateway.client.dto.RestartResponseDto;
import com.caloriebot.gateway.client.dto.StartConfigureResponseDto;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserServiceClientImpl.class)
@Import(RestClientConfig.class)
class UserServiceClientTest {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private MockRestServiceServer server;

    @Value("${services.user-service.url}")
    private String baseUrl;
    private final Long TG_ID = 1L;
    private static final UUID USER_ID = UUID.randomUUID();

    @Test
    void processingStart() {

        server.expect(requestTo(baseUrl + "/users/processing-start"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(
                    """
                      {"tgId": %d}
                    """.formatted(TG_ID)))
                .andRespond(withSuccess(
                        """
                            {
                              "userId": "%s",
                              "userState": "%s"
                            }
                        """.formatted(USER_ID, UserState.NEW.toString()),
                        MediaType.APPLICATION_JSON
                ));

        UserResponseDto response = userServiceClient.processingStart(new UserRequestDto(TG_ID));

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo(USER_ID);
        assertThat(response.userState()).isEqualTo(UserState.NEW.name());

        server.verify();
    }

    @Test
    void startConfigureWhenResponseApplied() {
        server.expect(requestTo(baseUrl + "/users/by-telegram/" + TG_ID + "/onboarding/start-configure"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(
                        """
                          {
                            "userState": "%s",
                            "applied": %s
                          }
                        """.formatted(UserState.WAITING_WEIGHT.toString(), true),
                        MediaType.APPLICATION_JSON
                ));

        StartConfigureResponseDto responseDto = userServiceClient.processingStateStartConfigure(TG_ID);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userState()).isEqualTo(UserState.WAITING_WEIGHT.name());
        assertThat(responseDto.applied()).isEqualTo(Boolean.TRUE);

        server.verify();
    }

    @Test
    void startConfigureWhenResponseNotApplied() {
        server.expect(requestTo(baseUrl + "/users/by-telegram/" + TG_ID + "/onboarding/start-configure"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                """
                                  {
                                    "userState": "%s",
                                    "applied": %s
                                  }
                                """.formatted(UserState.WAITING_HEIGHT.toString(), false)
                ));

        StartConfigureResponseDto responseDto = userServiceClient.processingStateStartConfigure(TG_ID);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userState()).isEqualTo(UserState.WAITING_HEIGHT.name());
        assertThat(responseDto.applied()).isEqualTo(Boolean.FALSE);

        server.verify();
    }

    @Test
    void restartOnboardingWhenResponseApplied() {
        server.expect(requestTo(baseUrl + "/users/by-telegram/" + TG_ID + "/onboarding/restart"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(
                        """
                          {
                            "userState": "%s",
                            "applied": %s
                          }
                        """.formatted(UserState.WAITING_WEIGHT.toString(), true),
                        MediaType.APPLICATION_JSON
                ));

        RestartResponseDto responseDto = userServiceClient.restartOnboarding(TG_ID);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userState()).isEqualTo(UserState.WAITING_WEIGHT.name());
        assertThat(responseDto.applied()).isEqualTo(Boolean.TRUE);

        server.verify();
    }

    @Test
    void restartOnboardingWhenResponseNotApplied() {
        server.expect(requestTo(baseUrl + "/users/by-telegram/" + TG_ID + "/onboarding/restart"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                        """
                          {
                            "userState": "%s",
                            "applied": %s
                          }
                        """.formatted(UserState.WAITING_HEIGHT.toString(), false)
                ));

        RestartResponseDto responseDto = userServiceClient.restartOnboarding(TG_ID);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userState()).isEqualTo(UserState.WAITING_HEIGHT.name());
        assertThat(responseDto.applied()).isEqualTo(Boolean.FALSE);

        server.verify();
    }
}