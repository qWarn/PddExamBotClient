package ru.qwarn.pddexambotclient.bot.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.botutils.RequestConstants;
import ru.qwarn.pddexambotclient.bot.executors.ExceptionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.QuestionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;

@Component
@Setter
@AllArgsConstructor
@Slf4j
public class CallbackHandler implements Handler {

    private final QuestionExecutor questionExecutor;
    private final RestTemplate restTemplate;
    private final TicketExecutor ticketExecutor;
    private final ExceptionExecutor exceptionExecutor;

    @Override
    public void handle(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        String callBackParam = callBackData.split(" ").length > 1 ? callBackData.split(" ")[1] : null;
        try {
            switch (callBackData.split(" ")[0]) {
                case "ticket" -> questionExecutor.executeFirstQuestionFromTicket(chatId, callBackParam);

                case "selected" -> questionExecutor.executeFirstQuestionFromSelected(chatId);

                case "backToTickets", "prevTickets" -> ticketExecutor.executeTickets(chatId, false);

                case "addToSelected" -> addQuestionToSelected(chatId, callBackParam);

                case "removeFromSelected" -> removeFromSelectedList(chatId, callBackParam);

                case "nextTickets" -> ticketExecutor.executeTickets(chatId, true);
                default -> {
                    //do  nothing
                }
            }
        }
        catch (HttpClientErrorException e){
            exceptionExecutor.executeHttpClientErrorException(chatId, e);
        }
        catch (TelegramApiException | JsonProcessingException e){
            log.error("Execute exception: ", e);
        }
    }

    private void addQuestionToSelected(long chatId, String questionId) {
        restTemplate.patchForObject(String.format(RequestConstants.ADD_TO_SELECTED_URI, chatId, questionId),
                null, String.class);
    }

    private void removeFromSelectedList(long chatId, String questionId) {
        restTemplate.patchForObject(String.format(RequestConstants.REMOVE_FROM_SELECTED_URI, chatId, questionId),
                null, String.class);
    }
}
