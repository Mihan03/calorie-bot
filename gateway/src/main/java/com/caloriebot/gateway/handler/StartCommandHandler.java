package com.caloriebot.gateway.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface StartCommandHandler {
    SendMessage processStartHandler(Long tgId, Long chatId);
}
