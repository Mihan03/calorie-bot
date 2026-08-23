package com.caloriebot.gateway;

import com.caloriebot.common.LoggingConstants;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.service.MessageService;
import com.caloriebot.gateway.service.StartCommandHandler;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.UUID;

/**
 * @author Mikhail Feoktistov
 */
@Component
@Slf4j
@Getter
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final String botToken;
    private final MessageService messageService;
    private final StartCommandHandler startCommandHandler;

    private final TelegramClient telegramClient;

    @Autowired
    public TelegramBot(@Value("${telegram.bot.token}") String botToken, MessageService messageService, StartCommandHandler startCommandHandler) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.messageService = messageService;
        this.startCommandHandler = startCommandHandler;
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
                Message message = update.getMessage();
                Long chatId = update.getMessage().getChatId();
                log.info("Received update from chatId={}", chatId);

                SendMessage sendMessage = messageService.getMessage(BotMessage.EXCEPTION_MESSAGE.getText(), chatId, null);
                try {
                    if (("/start").equals(message.getText())) {
                        sendMessage = startCommandHandler.processStartHandler(message.getFrom().getId(), chatId);
                    } else {
                        sendMessage = messageService.getMessage("Скоро...", chatId, null);
                    }

                } catch (RuntimeException e) {
                    log.error(e.getMessage(), e);
                }

                telegramClient.execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception", e);
        } finally {
            MDC.clear();
        }
    }

}
