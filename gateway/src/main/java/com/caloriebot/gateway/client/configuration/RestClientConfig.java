package com.caloriebot.gateway.client.configuration;

import com.caloriebot.gateway.client.exception.UserNotRegisteredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient userServiceRestClient(
            RestClient.Builder builder,
            @Value("${services.user-service.url}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                .requestInterceptor(new CorrelationIdInterceptor())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        status -> status == HttpStatus.NOT_FOUND,
                        (request, _) -> {
                            throw new UserNotRegisteredException(
                                    "User is not registered, uri=" + request.getURI());
                        }
                )
                .build();
    }
}
