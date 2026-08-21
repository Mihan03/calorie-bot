package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.*;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.entity.UserStateEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public UserDtoResponse processingStart(UserDtoRequest userDtoRequest) {
        UserEntity userEntity = userRepository.findByTgId(userDtoRequest.tgId())
                .orElse(null);

        if (userEntity == null) {
            userEntity = new UserEntity();
            UserStateEntity userStateEntity = new UserStateEntity();
            userStateEntity.setState(UserState.NEW);
            userStateEntity.setUser(userEntity);

            userEntity.setTgId(userDtoRequest.tgId());
            userEntity.setUserState(userStateEntity);

            userRepository.save(userEntity);
        }

        return userMapper.toUserDtoResponse(userEntity);
    }
}
