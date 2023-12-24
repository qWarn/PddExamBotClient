package ru.qwarn.pddexambotclient.bot.executors;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.botutils.RequestConstants;

@Component
@RequiredArgsConstructor
@Setter
public class TicketExecutor {

    private final RestTemplate restTemplate;
    private TelegramBot telegramBot;

    @SneakyThrows
    public void executeTickets(long chatId, boolean next) {
        telegramBot.execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId) + (next ? RequestConstants.TICKET_NEXT_PARAM : ""),
                null, SendMessage.class));
    }

    @SneakyThrows
    public void executeStartMessageOrTickets(long chatId) {
        telegramBot.execute(restTemplate.postForObject(String.format(RequestConstants.START_URI, chatId),
                null, SendMessage.class));
    }


}
