package com.caloriebot.gateway.service;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.UserRequestDto;
import com.caloriebot.gateway.client.dto.UserResponseDto;
import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.util.InlineKeyboardBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class StartCommandHandlerImpl implements StartCommandHandler {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartHandler(Long tgId, Long chatId) {
        UserRequestDto userRequestDto = new UserRequestDto(tgId);
        UserResponseDto user = userServiceClient.processingStart(userRequestDto);

        log.info("Был получен user={} ", user.toString());

        InlineKeyboardMarkup markup = new InlineKeyboardBuilder()
                .button(BotKeyboard.ONB_START).row().build();

        return messageService.getMessage(BotMessage.WELCOME_MESSAGE.getText(), chatId, markup);
    }
}
