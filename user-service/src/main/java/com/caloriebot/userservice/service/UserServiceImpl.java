package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.UserDto;
import com.caloriebot.userservice.exception.NotFoundExcepton;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.User;
import com.caloriebot.userservice.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserDto getUserByTgId(Long tgId) {
        User user = userRepository.findByTgId(tgId).orElseThrow(() -> new NotFoundExcepton(
                "User not found"
        ));

        return userMapper.toUserDto(user);
    }
}
