package com.caloriebot.gateway.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface StartConfigureCallbackHandler {
    SendMessage processStartConfigureHandler(Long tgId, Long chatId);
}
