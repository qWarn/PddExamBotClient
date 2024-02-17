package ru.qwarn.pddexambotclient.bot.executors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;

import java.util.List;

@Component
@Lazy
@RequiredArgsConstructor
public class NotificationExecutor {

    private final TelegramBot telegramBot;

    public void executeNotification(List<SendMessage> messages){
        messages.forEach(message -> {
            try {
                telegramBot.execute(message);
            } catch (TelegramApiException e) {
                //ignored
            }
        });
    }
}
