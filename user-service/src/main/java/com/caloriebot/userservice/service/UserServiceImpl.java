package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.*;
import com.caloriebot.userservice.exception.NotFoundException;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.repository.UserRepository;
import com.caloriebot.userservice.repository.UserStateRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserStateRepository userStateRepository;
    private final UserMapper userMapper;

    public UserDtoResponse getUserByTgId(Long tgId) {
        return userMapper.toUserDtoResponse(getUserEntity(tgId));
    }

    public StartConfigureResponseDto changeState(Long tgId, UserState fromState, UserState toState) {
        int rowUpdated = userStateRepository.changeState(tgId, fromState, toState);
        if (rowUpdated != 1) {
            UserEntity userEntity = getUserEntity(tgId);
            log.info("Source state is needed - {}, current state - {}", fromState, userEntity.getUserState().getState());
            return new StartConfigureResponseDto(userEntity.getUserState().getState(), false);
        }

        return new StartConfigureResponseDto(toState, true);
    }

    public UserEntity getUserEntity(Long tgId) {
        return userRepository.findByTgId(tgId).orElseThrow(() -> new NotFoundException(
                ErrorCode.USER_NOT_FOUND,
                "User with tgId=%d was not found".formatted(tgId)
        ));
    }
}
