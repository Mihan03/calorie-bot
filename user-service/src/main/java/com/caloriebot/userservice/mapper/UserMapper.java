package com.caloriebot.userservice.mapper;

import com.caloriebot.userservice.dto.UserDto;
import com.caloriebot.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);
}
