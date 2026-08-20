package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.*;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.UserEntity;
import com.caloriebot.userservice.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDtoResponse getUserByTgId(Long tgId) {
        UserEntity userEntity = userRepository.findByTgId(tgId).orElseThrow(() -> new NotFoundException(
                ErrorCode.USER_NOT_FOUND,
                "User with tgId=%d was not found".formatted(tgId)
        ));

        return userMapper.toUserDtoResponse(userEntity);
    }
}
