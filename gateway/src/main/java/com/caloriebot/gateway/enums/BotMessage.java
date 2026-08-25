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

            Для начала давай определим твою дневную цель.""",
            "Приветствие",
            UserState.NEW),
    WAITING_WEIGHT("Шаг 1 из 6. Укажи свой вес в килограммах, например: 78,5", "вес", UserState.WAITING_WEIGHT),
    WAITING_HEIGHT("Шаг 2 из 6. Укажи рост в сантиметрах — например: 180", "рост", UserState.WAITING_HEIGHT),

    CURRENT_STEP("Настройка уже идет, вы на шаге ", null, null),
    EXCEPTION_MESSAGE("Что-то пошло не то, повторите попытку попозже...", null, null);

    private final String text;
    private final String shortName;
    private final UserState userState;
}
