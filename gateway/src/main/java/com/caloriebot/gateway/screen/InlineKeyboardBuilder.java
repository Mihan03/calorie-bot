package com.caloriebot.gateway.screen;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {
    private final List<InlineKeyboardButton> buttons = new ArrayList<>();
    private final List<InlineKeyboardRow> rows = new ArrayList<>();

    public InlineKeyboardBuilder button(BotKeyboard keyboard) {
        buttons.add(
                InlineKeyboardButton.builder()
                        .text(keyboard.getText())
                        .callbackData(keyboard.getCallback())
                        .build()
        );

        return this;
    }

    public InlineKeyboardBuilder buttons(List<BotKeyboard> keyboards) {
        keyboards.forEach(kb -> this.button(kb).row());
        return this;
    }

    public InlineKeyboardBuilder row() {
        rows.add(new InlineKeyboardRow(buttons));
        buttons.clear();
        return this;
    }

    public InlineKeyboardMarkup build() {
        row();
        return new InlineKeyboardMarkup(rows);
    }
}