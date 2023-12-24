package ru.qwarn.pddexambotclient.bot.handlers;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.executors.QuestionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;

@Setter
@AllArgsConstructor
@Component
public class MessageHandler implements Handler {
    private final QuestionExecutor questionExecutor;
    private final TicketExecutor ticketExecutor;

    @SneakyThrows
    @Override
    public void handle(Update update) {
        String messageFromUser = update.getMessage().getText().toLowerCase();
        long chatId = update.getMessage().getChatId();

        switch (messageFromUser) {
            case "/start" -> ticketExecutor.executeStartMessageOrTickets(chatId);

            case "выйти" -> ticketExecutor.executeTickets(chatId, false);

            default -> questionExecutor.executeQuestionAnswer(chatId, messageFromUser);
        }
    }
}
