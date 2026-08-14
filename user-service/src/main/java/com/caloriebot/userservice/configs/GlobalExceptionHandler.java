package com.caloriebot.userservice.configs;

import com.caloriebot.common.LoggingConstants;
import com.caloriebot.userservice.dto.ApiError;
import com.caloriebot.userservice.dto.ApiErrorResponse;
import com.caloriebot.userservice.exception.NotFoundException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiErrorResponse handleNotFound(NotFoundException ex) {
        ApiError error = new ApiError(
                ex.getErrorCode().name(),
                ex.getMessage(),
                ex.getErrorCode().getUserMessage(),
                null
        );

        return new ApiErrorResponse(MDC.get(LoggingConstants.CORRELATION_ID), List.of(error));
    }
}
