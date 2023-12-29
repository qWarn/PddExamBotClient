package ru.qwarn.pddexambotclient.bot.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.executors.ExceptionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.QuestionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;

@Setter
@AllArgsConstructor
@Component
@Slf4j
public class MessageHandler implements Handler {
    private final QuestionExecutor questionExecutor;
    private final TicketExecutor ticketExecutor;
    private final ExceptionExecutor exceptionExecutor;

    @Override
    public void handle(Update update)  {
        String messageFromUser = update.getMessage().getText().toLowerCase();
        long chatId = update.getMessage().getChatId();
        try {
            switch (messageFromUser) {
                case "/start" -> ticketExecutor.executeStartMessageOrTickets(chatId);

                case "выйти" -> ticketExecutor.executeTickets(chatId, false);

                default -> questionExecutor.executeQuestionAnswer(chatId, messageFromUser);
            }

        }catch (HttpClientErrorException e){
            exceptionExecutor.executeHttpClientErrorException(chatId, e);
        }
        catch (TelegramApiException | JsonProcessingException e) {
            log.error("Execute exception: ", e);
        }
    }
}
