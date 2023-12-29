package ru.qwarn.pddexambotclient.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Handler {
    void handle(Update update) throws TelegramApiException;
}
