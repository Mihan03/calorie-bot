package com.caloriebot.gateway.handler;

import com.caloriebot.gateway.client.UserServiceClient;
import com.caloriebot.gateway.client.dto.StartConfigureDtoResponse;
import com.caloriebot.gateway.screen.BotKeyboard;
import com.caloriebot.gateway.screen.BotMessage;
import com.caloriebot.gateway.UserState;
import com.caloriebot.gateway.service.MessageService;
import com.caloriebot.gateway.screen.Screen;
import com.caloriebot.gateway.screen.StateMap;
import com.caloriebot.gateway.screen.InlineKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartConfigureCallbackHandlerImpl implements StartConfigureCallbackHandler {
    private final UserServiceClient userServiceClient;
    private final MessageService messageService;

    public SendMessage processStartConfigureHandler(Long tgId, Long chatId) {
        StartConfigureDtoResponse response = userServiceClient.processingStateStartConfigure(tgId);

        if (!response.applied()) {
            UserState currentState = UserState.valueOf(response.userState());

            Screen screen = StateMap.getScreen(currentState);

            InlineKeyboardMarkup markup = new InlineKeyboardBuilder().button(BotKeyboard.ONB_RESTART).build();
            return messageService.getMessage(BotMessage.CURRENT_STEP.getText() + "\"" + screen.message().getShortName() + "\". " +
                    screen.message().getText(), chatId, markup);
        }

        return messageService.getMessageByScreen(StateMap.getScreen(UserState.WAITING_WEIGHT), chatId);
    }
}
