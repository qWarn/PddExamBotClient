package ru.qwarn.pddexambotclient.bot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.TelegramBot;

@RestController
@RequiredArgsConstructor
public class WebhookController {

    private final TelegramBot telegramBot;

    @PostMapping("/callback/update")
    public void handleUpdate(@RequestBody Update update) {
        telegramBot.onWebhookUpdateReceived(update);
    }
}
