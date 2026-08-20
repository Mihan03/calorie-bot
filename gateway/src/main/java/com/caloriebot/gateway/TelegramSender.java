package com.caloriebot.gateway;

import com.caloriebot.common.LoggingConstants;
import com.caloriebot.gateway.service.MessageService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.UUID;

/**
 * @author Mikhail Feoktistov
 */
@Component
@Slf4j
public class TelegramSender implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Getter
    private final String botToken;

    @Autowired
    private MessageService messageService;

    private final TelegramClient telegramClient;

    public TelegramSender(@Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        MDC.put(LoggingConstants.CORRELATION_ID, UUID.randomUUID().toString());
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                telegramClient.execute(messageService.getMessage(update));
            }
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception", e);
        } finally {
            MDC.clear();
        }
    }

}
