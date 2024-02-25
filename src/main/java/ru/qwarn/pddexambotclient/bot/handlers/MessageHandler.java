package ru.qwarn.pddexambotclient.bot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;

@Setter
@RequiredArgsConstructor
@Component
public class MessageHandler implements Handler {

    private final TicketExecutor ticketExecutor;

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        int messageId = update.getMessage().getMessageId();
        String message = update.getMessage().getText().toLowerCase();
        try {
            if (message.equals("/start")) {
                ticketExecutor.executeStartMessageOrTickets(chatId);
            } else {
                ticketExecutor.deleteMessage(chatId, messageId);
            }
        } catch (Exception e) {
            //ignored
        }
    }
}
