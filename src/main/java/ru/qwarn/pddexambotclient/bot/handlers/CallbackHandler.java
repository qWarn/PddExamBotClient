package ru.qwarn.pddexambotclient.bot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.dto.TaskDTO;
import ru.qwarn.pddexambotclient.bot.executors.QuestionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;

import static ru.qwarn.pddexambotclient.bot.constants.CallbackConstants.*;
import static ru.qwarn.pddexambotclient.bot.dto.TaskType.*;

@Component
@RequiredArgsConstructor
@Setter
@Slf4j
public class CallbackHandler implements Handler {

    private final QuestionExecutor questionExecutor;
    private final TicketExecutor ticketExecutor;

    @Override
    public void handle(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String callback = update.getCallbackQuery().getData();
        String event = callback.split(" ")[0];
        String attribute = callback.split(" ").length > 1 ? callback.split(" ")[1] : null;
        try {
            switch (event) {
                case START_TICKET -> ticketExecutor.startTask(chatId, messageId, new TaskDTO(TICKET, Integer.parseInt(attribute)));

                case START_SELECTED -> ticketExecutor.startTask(chatId, messageId, new TaskDTO(SELECTED, 0));

                case GET_TICKETS -> ticketExecutor.executeTickets(chatId, messageId, false);

                case ADD_TO_SELECTED -> questionExecutor.addQuestionToSelected(chatId, attribute);

                case REMOVE_FROM_SELECTED -> questionExecutor.removeFromSelectedList(chatId, attribute);

                case GET_MORE_TICKETS -> ticketExecutor.executeTickets(chatId, messageId, true);

                case GET_QUESTION -> questionExecutor.executeQuestion(chatId, attribute, messageId);

                case GET_ANSWER -> questionExecutor.executeAnswer(chatId, messageId);

                default -> {
                    //do  nothing
                }
            }
        } catch (Exception e) {
            //ignored
        }
    }

}
