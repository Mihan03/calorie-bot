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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDtoResponse getUserByTgId(Long tgId) {
        return userMapper.toUserDtoResponse(getUserEntity(tgId));
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

            log.info("Был зарегистрирован пользователь с tgId={}, присвоено состояние={}",
                    userDtoRequest.tgId(), UserState.NEW.name());
        }

        log.info("Получен пользователь={}", userEntity);

        return userMapper.toUserDtoResponse(userEntity);
    }

    @Transactional
    public StartConfigureDtoResponse processingStateStartConfigure(Long tgId) {
        UserEntity userEntity = getUserEntity(tgId);

        int rowUpdated = userRepository.changeState(userEntity, UserState.NEW, UserState.WAITING_WEIGHT);
        if (rowUpdated != 1) {
            log.error("Требуемое состояние - {}, текущее - {}", UserState.WAITING_WEIGHT.name(), userEntity.getUserState().getState());
            return new StartConfigureDtoResponse(userEntity.getUserState().getState(), false);
        }

        log.info("User state was changed from {} to {}", UserState.NEW.name(), UserState.WAITING_WEIGHT.name());
        return new StartConfigureDtoResponse(UserState.WAITING_WEIGHT, true);
    }

    private UserEntity getUserEntity(Long tgId) {
        return userRepository.findByTgId(tgId).orElseThrow(() -> new NotFoundException(
                ErrorCode.USER_NOT_FOUND,
                "User with tgId=%d was not found".formatted(tgId)
        ));
    }
}
