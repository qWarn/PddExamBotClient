package ru.qwarn.pddexambotclient.bot.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final ObjectMapper objectMapper;

    private final TelegramBot telegramBot;

    @KafkaListener(topics = "${kafka.topics.notificationTopic}", groupId = "1")
    public void sendNotification(String message) throws JsonProcessingException, TelegramApiException {
        SendMessage sendMessage = objectMapper.readValue(message, SendMessage.class);
        telegramBot.execute(sendMessage);
    }
}
