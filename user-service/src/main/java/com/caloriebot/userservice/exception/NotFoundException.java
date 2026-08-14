package com.caloriebot.userservice.exception;

import com.caloriebot.userservice.dto.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    protected ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode, String technicalMessage) {
        super(technicalMessage);
        this.errorCode = errorCode;
    }
}
