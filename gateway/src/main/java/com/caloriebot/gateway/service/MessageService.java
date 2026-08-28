package com.caloriebot.gateway.service;

import com.caloriebot.gateway.screen.Screen;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 *
 * @author Mikhail Feoktistov
 */
public interface MessageService {
    SendMessage getMessage(String textMessage,
                           Long chatId,
                           InlineKeyboardMarkup markup);

    SendMessage getMessageByScreen(Screen screen, Long chatId);
}