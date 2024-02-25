package ru.qwarn.pddexambotclient.bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WebhookSetup implements CommandLineRunner {

    private final TelegramConfig telegramConfig;
    private final RestTemplate restTemplate;


    @Override
    public void run(String... args) {
        String botToken = telegramConfig.getToken();
        String webhookUrl = telegramConfig.getBotUri();

        String setWebhookUrl = "https://api.telegram.org/bot" + botToken + "/setWebhook?url=" + webhookUrl;

        restTemplate.getForObject(setWebhookUrl, String.class);
    }
}
