package com.caloriebot.gateway.service;

import com.caloriebot.gateway.screen.Screen;
import com.caloriebot.gateway.screen.InlineKeyboardBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
public class MessageServiceImpl implements MessageService {
    public SendMessage getMessage(String textMessage,
                                  Long chatId,
                                  InlineKeyboardMarkup markup) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(textMessage)
                .replyMarkup(markup)
                .build();

        writeLog(textMessage, chatId);

        return message;
    }

    public SendMessage getMessageByScreen(Screen screen, Long chatId) {
        return getMessageByScreen(screen, chatId, "");
    }

    public SendMessage getMessageByScreen(Screen screen, Long chatId, String prefix) {

        InlineKeyboardMarkup markup = new InlineKeyboardBuilder()
                .buttons(screen.keyboards()).build();

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(prefix + screen.message().getText())
                .replyMarkup(markup)
                .build();

        writeLog(screen.message().getText(), chatId);

        return message;
    }

    private void writeLog(String text, Long chatId) {
        log.info("Сформировано сообщение с текстом ={} в chatId={} ", text, chatId);
    }
}