package com.caloriebot.userservice.controller;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Getter
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/by-telegram/{tgId}")
    public ResponseEntity<UserDtoResponse> getUserByTgId(@PathVariable Long tgId) {
        return ResponseEntity.ok(userService.getUserByTgId(tgId));
    }

    @PostMapping(value = "/processing-start")
    public ResponseEntity<UserDtoResponse> processingStart(@RequestBody @Validated UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.processingStart(userDtoRequest));
    }

    @PostMapping(value = "/by-telegram/{tgId}/onboarding/start-configure")
    public ResponseEntity<StartConfigureResponseDto> processingStateStartConfigure(@PathVariable Long tgId) {
        StartConfigureResponseDto response = userService.processingStateStartConfigure(tgId);

        if (!response.applied()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "by-telegram/{tgId}/onboarding/restart")
    public ResponseEntity<RestartResponseDto> restart(@PathVariable Long tgId) {
        RestartResponseDto response = userService.restartOnboarding(tgId);

        if (!response.applied()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
