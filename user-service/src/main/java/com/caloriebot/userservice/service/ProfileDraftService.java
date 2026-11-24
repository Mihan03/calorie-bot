package com.caloriebot.userservice.service;

import com.caloriebot.userservice.model.entity.ProfileDraftEntity;
import com.caloriebot.userservice.model.entity.UserEntity;

public interface ProfileDraftService {
    ProfileDraftEntity updateProfileDraft(ProfileDraftEntity profileDraftEntity, Long tgId);
    ProfileDraftEntity getProfileDraft(UserEntity user);
}
