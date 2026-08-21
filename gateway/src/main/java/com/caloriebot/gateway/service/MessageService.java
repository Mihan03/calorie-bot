package com.caloriebot.gateway.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *
 * @author Mikhail Feoktistov
 */
public interface MessageService {
    SendMessage getMessage(Update update);
}
