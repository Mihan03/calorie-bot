package com.caloriebot.gateway.screen;

import java.util.List;

/**
 * Описание экрана бота для конкретного состояния диалога.
 *
 * @param message        текст шага
 * @param conflictPrefix шапка, которая приписывается перед текстом шага,
 *                       когда действие пользователя недопустимо в этом состоянии
 * @param keyboards      кнопки экрана
 */
public record Screen(
        BotMessage message,
        BotMessage conflictPrefix,
        List<BotKeyboard> keyboards
) {}
