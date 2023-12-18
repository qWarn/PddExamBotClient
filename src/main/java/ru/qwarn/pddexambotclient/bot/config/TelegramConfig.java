package ru.qwarn.pddexambotclient.bot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramConfig {

    @Value("${bot.botUri}")
    String webhookPath;
    @Value("${bot.botName}")
    String botName;
    @Value("${bot.token}")
    String botToken;
}
