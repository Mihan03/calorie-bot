package com.caloriebot.userservice.controller;

import com.caloriebot.userservice.dto.UserDto;
import com.caloriebot.userservice.service.UserServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/users")
@RequiredArgsConstructor
@Getter
public class UserController {

    private UserServiceImpl userService;

    @GetMapping(value = "/{tgId}")
    public ResponseEntity<UserDto> getUserByTgId(@PathVariable Long tgId) {
        return ResponseEntity.ok(userService.getUserByTgId(tgId));
    }
}
