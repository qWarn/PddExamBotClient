package ru.qwarn.PddExamBotClient.bot.handlers;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.PddExamBotClient.bot.botutils.RequestConstants;
import ru.qwarn.PddExamBotClient.bot.executors.QuestionExecutor;
import ru.qwarn.PddExamBotClient.bot.executors.TicketExecutor;

@Component
@Setter
@AllArgsConstructor
public class CallbackHandler implements Handler {

    private final QuestionExecutor questionExecutor;
    private final RestTemplate restTemplate;
    private final TicketExecutor ticketExecutor;

    @SneakyThrows
    @Override
    public void handle(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        String callBackParam = callBackData.split(" ").length > 1 ? callBackData.split(" ")[1] : null;

        switch (callBackData.split(" ")[0]){
            case "ticket" ->
                    questionExecutor.executeFirstQuestionFromTicket(chatId, callBackParam);

            case "selected" ->
                    questionExecutor.executeFirstQuestionFromSelected(chatId);

            case "backToTickets", "prevTickets" ->
                    ticketExecutor.executeTickets(chatId, false);

            case "addToSelected" ->
                    addQuestionToSelected(chatId, callBackParam);

            case "removeFromSelected" ->
                    removeFromSelectedList(chatId, callBackParam);

            case "nextTickets" ->
                    ticketExecutor.executeTickets(chatId, true);

        }
    }

    private void addQuestionToSelected(long chatId, String questionId){
        restTemplate.patchForObject(String.format(RequestConstants.ADD_TO_SELECTED_URI, chatId, questionId),
                null, String.class);
    }

    private void removeFromSelectedList(long chatId, String questionId){
        restTemplate.patchForObject(String.format(RequestConstants.REMOVE_FROM_SELECTED_URI, chatId, questionId),
                null, String.class);
    }
}
