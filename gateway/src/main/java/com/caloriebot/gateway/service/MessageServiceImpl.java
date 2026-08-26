package com.caloriebot.gateway.service;

import com.caloriebot.gateway.util.InlineKeyboardBuilder;
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

        log.info("Сформировано сообщение с текстом ={} в chatId={} ", textMessage, chatId);

        return message;
    }

    public SendMessage getMessageByScreen(Screen screen, Long chatId) {

        InlineKeyboardMarkup markup = new InlineKeyboardBuilder()
                .buttons(screen.getKeyboards()).build();

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(screen.getMessage().getText())
                .replyMarkup(markup)
                .build();

        log.info("Сформировано сообщение с текстом ={} в chatId={} ", screen.getMessage().getText(), chatId);

        return message;
    }
}