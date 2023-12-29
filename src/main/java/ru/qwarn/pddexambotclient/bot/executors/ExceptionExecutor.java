package ru.qwarn.pddexambotclient.bot.executors;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.botutils.ExceptionMessageCreator;

@Component
@Slf4j
@Setter
public class ExceptionExecutor {
    private TelegramBot telegramBot;

    public void executeHttpClientErrorException(long chatId, HttpClientErrorException e) {
        try {
            telegramBot.execute(ExceptionMessageCreator.createExceptionMessage(chatId, e));
        } catch (TelegramApiException ex) {
            log.error("Execute fail: ", ex);
        }
    }


}
