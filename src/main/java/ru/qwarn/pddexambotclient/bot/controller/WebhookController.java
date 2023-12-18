package ru.qwarn.pddexambotclient.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.TelegramBot;

@RestController
public class WebhookController {

    private final TelegramBot telegramBot;
    @Autowired
    public WebhookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/callback/update")
    public void handleUpdate(@RequestBody Update update){
        telegramBot.onWebhookUpdateReceived(update);
    }
}
