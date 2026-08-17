package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.UserDtoResponse;

public interface UserService {
    UserDtoResponse getUserByTgId(Long tgId);
}