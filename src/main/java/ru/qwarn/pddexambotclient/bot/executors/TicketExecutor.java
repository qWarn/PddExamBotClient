package ru.qwarn.pddexambotclient.bot.executors;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.dto.TaskDTO;

import static ru.qwarn.pddexambotclient.bot.constants.RequestConstants.*;
@Component
@RequiredArgsConstructor
@Setter
public class TicketExecutor {

    private final RestTemplate restTemplate;

    private TelegramBot telegramBot;

    public void executeTickets(long chatId, int messageId, boolean next) throws TelegramApiException {
        deleteMessage(chatId, messageId);
        telegramBot.execute(restTemplate.patchForObject(String.format(GET_TICKETS_URI, chatId) + (next ? GET_MORE_TICKETS_URI : ""),
                null, SendMessage.class));
    }

    public void executeStartMessageOrTickets(long chatId) throws TelegramApiException {
        telegramBot.execute(restTemplate.postForObject(String.format(START_BOT_URI, chatId),
                null, SendMessage.class));
    }

    public void startTask(long chatId, int messageId, TaskDTO taskDTO) throws TelegramApiException {
        deleteMessage(chatId, messageId);
        telegramBot.execute(restTemplate.exchange(
                RequestEntity.patch(String.format(START_TASK_URI, chatId)).body(taskDTO),
                SendMessage.class)
                .getBody());
    }

    public void deleteMessage(long chatId, int messageId) throws TelegramApiException {
        telegramBot.execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build());
    }

}
