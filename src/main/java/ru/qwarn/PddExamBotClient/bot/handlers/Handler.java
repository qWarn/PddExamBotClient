package ru.qwarn.PddExamBotClient.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void handle(Update update);
}
