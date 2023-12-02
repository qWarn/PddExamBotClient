package ru.qwarn.PddExamBotClient.bot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WebhookSetup implements CommandLineRunner{

    private final TelegramConfig telegramConfig;

    public WebhookSetup(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }

    @Override
    public void run(String... args) {
        String botToken = telegramConfig.getBotToken();
        String webhookUrl = telegramConfig.getWebhookPath();

        String setWebhookUrl = "https://api.telegram.org/bot" + botToken + "/setWebhook?url=" + webhookUrl;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(setWebhookUrl, String.class);

        System.out.println("Webhook setup response: " + response);
    }
}
