package ru.qwarn.pddexambotclient.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void handle(Update update);
}
