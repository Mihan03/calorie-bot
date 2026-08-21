package com.caloriebot.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

import static com.caloriebot.gateway.enums.BotMessage.WELCOME_MESSAGE;

@Slf4j
@Component
public class MessageServiceImpl implements MessageService {
    public SendMessage getMessage(Update update) {

        String messageText = update.getMessage().getText();
        String text;
        long chatId = update.getMessage().getChatId();
        log.info("Received update from chatId={}", chatId);

        InlineKeyboardMarkup markup = null;

        if (messageText.equals("/start")) {
            text = WELCOME_MESSAGE.getText();

            InlineKeyboardButton button = new InlineKeyboardButton("Начать настройку");
            button.setCallbackData("start_onboarding");

            List<InlineKeyboardRow> rows = List.of(new InlineKeyboardRow(button));
            markup = new InlineKeyboardMarkup(rows);
        } else {
            text = "Скоро...";
        }

        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }
}
