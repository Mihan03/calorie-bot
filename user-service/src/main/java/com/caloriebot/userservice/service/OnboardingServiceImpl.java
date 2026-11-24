package com.caloriebot.userservice.service;

import com.caloriebot.userservice.dto.RestartResponseDto;
import com.caloriebot.userservice.dto.StartConfigureResponseDto;
import com.caloriebot.userservice.dto.UserDtoRequest;
import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.mapper.UserMapper;
import com.caloriebot.userservice.model.entity.ProfileDraftEntity;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.model.entity.UserStateEntity;
import com.caloriebot.userservice.model.enums.UserState;
import com.caloriebot.userservice.repository.UserRepository;
import com.caloriebot.userservice.repository.UserStateRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final UserService userService;
    private final ProfileDraftService profileDraftService;
    private final UserRepository userRepository;
    private final UserStateRepository userStateRepository;
    private final UserMapper userMapper;

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
        ProfileDraftEntity profileDraftEntity = new ProfileDraftEntity();
        profileDraftEntity.setUser(userEntity);
        profileDraftService.updateProfileDraft(profileDraftEntity, userEntity.getTgId());

        return userMapper.toUserDtoResponse(userEntity);
    }

    @Override
    @Transactional
    public StartConfigureResponseDto processingStateStartConfigure(Long tgId) {
        StartConfigureResponseDto responseDto = userService.changeState(tgId, UserState.NEW, UserState.WAITING_WEIGHT);

        if (!responseDto.applied()) {
            return responseDto;
        }

        ProfileDraftEntity profileDraftEntity = new ProfileDraftEntity();

        profileDraftService.updateProfileDraft(profileDraftEntity, tgId);

        log.info("User state was changed from {} to {}", UserState.NEW.name(), UserState.WAITING_WEIGHT.name());
        return new StartConfigureResponseDto(UserState.WAITING_WEIGHT, true);
    }

    @Override
    @Transactional
    public RestartResponseDto restartOnboarding(Long tgId) {
        int rowsUpdated = userStateRepository.restartOnboarding(
                tgId,
                UserState.getOnboardingStates(),
                UserState.WAITING_WEIGHT
        );

        if (rowsUpdated != 1) {
            UserEntity user = userService.getUserEntity(tgId);
            log.info("User {} exists, but its onboarding state was not updated", user);
            return new RestartResponseDto(user.getUserState().getState(), false);
        }

        return new RestartResponseDto(UserState.WAITING_WEIGHT, true);
    }
}
