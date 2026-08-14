package com.caloriebot.userservice.controller;

import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
