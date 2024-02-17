package ru.qwarn.pddexambotclient.bot.handlers;

import lombok.AllArgsConstructor;
import lombok.Setter;
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

    @Override
    public void handle(Update update) {
        String messageFromUser = update.getMessage().getText().toLowerCase();
        long chatId = update.getMessage().getChatId();
        try {
            switch (messageFromUser) {
                case "/start" -> ticketExecutor.executeStartMessageOrTickets(chatId);

                case "к билетам" -> ticketExecutor.executeTickets(chatId, false);

                default -> questionExecutor.executeQuestionAnswerAndNextQuestion(chatId, messageFromUser);
            }

        } catch (Exception e) {
            //ignored
        }
    }
}
