package com.caloriebot.gateway;

import com.caloriebot.common.LoggingConstants;
import com.caloriebot.gateway.enums.BotKeyboard;
import com.caloriebot.gateway.enums.BotMessage;
import com.caloriebot.gateway.service.MessageService;
import com.caloriebot.gateway.service.StartCommandHandler;
import com.caloriebot.gateway.service.StartConfigureCallbackHandler;
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
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
    private final StartConfigureCallbackHandler startConfigureCallbackHandler;

    private final TelegramClient telegramClient;

    @Autowired
    public TelegramBot(@Value("${telegram.bot.token}") String botToken, MessageService messageService, StartCommandHandler startCommandHandler, StartConfigureCallbackHandler startConfigureCallbackHandler) {
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.messageService = messageService;
        this.startCommandHandler = startCommandHandler;
        this.startConfigureCallbackHandler = startConfigureCallbackHandler;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        MDC.put(LoggingConstants.CORRELATION_ID, UUID.randomUUID().toString());
        try {
            SendMessage sendMessage = null;
            BaseTelegramData telegramData = extractBaseData(update);
            Long chatId = telegramData.chatId();
            Long tgId = telegramData.tgId();

            if (!telegramData.isCallback) {
                Message message = update.getMessage();
                String text = message.getText();
                log.info("Received update from chatId={}, text={}", chatId, text);

                try {
                    if (("/start").equals(text)) {
                        sendMessage = startCommandHandler.processStartHandler(tgId, chatId);
                    } else {
                        sendMessage = messageService.getMessage("Скоро...", chatId, null);
                    }

                } catch (RuntimeException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (telegramData.isCallback) {
                String callbackQuery = update.getCallbackQuery().getData();

                sendAnswerCallbackQuery(update.getCallbackQuery().getId());

                log.info("Received update from chatId={}, callback={}, tgId={}", chatId, callbackQuery, tgId);
                try {
                    if (BotKeyboard.ONB_START.getCallback().equals(callbackQuery)) {
                        sendMessage = startConfigureCallbackHandler.processStartConfigureHandler(tgId, chatId);
                    } else if (BotKeyboard.ONB_RESTART.getCallback().equals(callbackQuery)) {
                        sendMessage = startCommandHandler.processRestartHandler(tgId, chatId);
                    }
                } catch (RuntimeException e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                sendMessage = messageService.getMessage(BotMessage.EXCEPTION_MESSAGE.getText(), chatId, null);
            }

            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception", e);
        } finally {
            MDC.clear();
        }
    }

    private BaseTelegramData extractBaseData(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return new BaseTelegramData(
                    update.getMessage().getChatId(),
                    update.getMessage().getFrom().getId(),
                    false
            );
        } else if (update.hasCallbackQuery()) {
            return new BaseTelegramData(
                    update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getFrom().getId(),
                    true
            );
        }

        return null;
    }

    private void sendAnswerCallbackQuery(String callbackId) throws TelegramApiException {
        telegramClient.execute(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackId)
                        .build()
        );
    }

    private record BaseTelegramData(
        Long chatId,
        Long tgId,
        boolean isCallback
    ) {}
}
