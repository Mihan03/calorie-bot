package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.UserDto;

public interface UserService {
    UserDto getUserByTgId(Long tgId);
}