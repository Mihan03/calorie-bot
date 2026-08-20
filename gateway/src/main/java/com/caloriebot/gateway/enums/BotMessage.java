package com.caloriebot.gateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BotMessage {
    WELCOME_MESSAGE("""
            Привет! 👋 Я помогу считать калории по фото еды.

            Как это работает:
            📸 Отправляешь фото тарелки — я распознаю блюдо и посчитаю КБЖУ
            📊 Показываю, сколько осталось до дневной нормы
            📈 Веду дневник: статистика за день, неделю, месяц

            Для начала давай определим твою дневную цель.""");

    private final String text;
}
