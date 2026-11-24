package com.caloriebot.userservice.service;

import com.caloriebot.userservice.model.entity.ProfileDraftEntity;
import com.caloriebot.userservice.model.entity.UserEntity;
import com.caloriebot.userservice.repository.ProfileDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileDraftServiceImpl implements ProfileDraftService {
    private final ProfileDraftRepository profileDraftRepository;
    private final UserService userService;

    @Transactional
    public ProfileDraftEntity updateProfileDraft(ProfileDraftEntity profileDraftToUpdate, Long tgId) {
        UserEntity user = userService.getUserEntity(tgId);

        ProfileDraftEntity profileDraftCurrently = getProfileDraft(user);

        profileDraftCurrently.setUser(user);

        if (profileDraftToUpdate.getAge() != null) {
            profileDraftCurrently.setAge(profileDraftToUpdate.getAge());
        }

        if (profileDraftToUpdate.getGender() != null) {
            profileDraftCurrently.setGender(profileDraftToUpdate.getGender());
        }

        if (profileDraftToUpdate.getWeight() != null) {
            profileDraftCurrently.setWeight(profileDraftToUpdate.getWeight());
        }

        if (profileDraftToUpdate.getHeight() != null) {
            profileDraftCurrently.setHeight(profileDraftToUpdate.getHeight());
        }

        return profileDraftRepository.save(profileDraftCurrently);
    }


    public ProfileDraftEntity getProfileDraft(UserEntity user) {
        return profileDraftRepository.findByUser(user).orElse(new ProfileDraftEntity());
    }

    public ProfileDraftEntity clearProfileDraft(ProfileDraftEntity profileDraft) {
        profileDraft.setAge(null);
        profileDraft.setGender(null);
        profileDraft.setWeight(null);
        profileDraft.setHeight(null);
        return profileDraftRepository.save(profileDraft);
    }
}
