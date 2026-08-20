package com.caloriebot.userservice.mapper;

import com.caloriebot.userservice.dto.UserDtoResponse;
import com.caloriebot.userservice.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(source = "id", target = "userId")
    UserDtoResponse toUserDtoResponse(UserEntity userEntity);
}