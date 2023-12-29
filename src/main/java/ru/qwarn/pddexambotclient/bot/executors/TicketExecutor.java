package ru.qwarn.pddexambotclient.bot.executors;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.botutils.RequestConstants;

@Component
@RequiredArgsConstructor
@Setter
public class TicketExecutor {

    private final RestTemplate restTemplate;
    private TelegramBot telegramBot;

    public void executeTickets(long chatId, boolean next) throws TelegramApiException {
        telegramBot.execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId) + (next ? RequestConstants.TICKET_NEXT_PARAM : ""),
                null, SendMessage.class));
    }

    public void executeStartMessageOrTickets(long chatId) throws TelegramApiException {

        telegramBot.execute(restTemplate.postForObject(String.format(RequestConstants.START_URI, chatId),
                null, SendMessage.class));

    }


}
