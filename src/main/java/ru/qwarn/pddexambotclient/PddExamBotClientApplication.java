package ru.qwarn.pddexambotclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.qwarn.pddexambotclient.bot.config.TelegramConfig;

@SpringBootApplication
@EnableConfigurationProperties(TelegramConfig.class)
public class PddExamBotClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PddExamBotClientApplication.class, args);
    }


}
