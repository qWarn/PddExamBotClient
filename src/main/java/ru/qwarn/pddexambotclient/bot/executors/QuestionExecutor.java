package ru.qwarn.pddexambotclient.bot.executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.botutils.ExceptionMessageCreator;
import ru.qwarn.pddexambotclient.bot.botutils.RequestConstants;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Setter
public class QuestionExecutor {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private TelegramBot telegramBot;

    private void executeQuestion(String responseMessage) throws JsonProcessingException, TelegramApiException {
        String methodName = StringUtils.substringBefore(StringUtils.substringAfter(responseMessage, "\"method\":\""), "\"}");
        if (methodName.equals("sendphoto")) {
            telegramBot.execute(objectMapper.readValue(responseMessage, SendPhoto.class));
        } else {
            telegramBot.execute(objectMapper.readValue(responseMessage, SendMessage.class));
        }
    }

    public void executeQuestionAnswer(long chatId, String answerNumber) throws TelegramApiException, JsonProcessingException {
        if (answerNumber.matches("^-?\\d+")) {
            try {
                telegramBot.execute(restTemplate.getForObject(String.format(RequestConstants.ANSWER_URI, chatId, answerNumber), SendMessage.class));
                executeQuestion(restTemplate.patchForObject(String.format(RequestConstants.QUESTION_URI, chatId), null, String.class));
            } catch (HttpClientErrorException e) {
                telegramBot.execute(ExceptionMessageCreator.createExceptionMessage(chatId, e));
            }
        } else {
            telegramBot.execute(SendMessage.builder().text("Выбирите число.").chatId(chatId).build());
        }
    }

    public void executeFirstQuestionFromSelected(long chatId) throws TelegramApiException, JsonProcessingException {

        executeQuestion(Objects.requireNonNull(restTemplate.patchForObject(String.format(RequestConstants.SELECTED_URI, chatId), null, String.class)));

    }

    public void executeFirstQuestionFromTicket(long chatId, String ticketNumber) throws TelegramApiException, JsonProcessingException {
        String responseMessage = Objects.requireNonNull(restTemplate.patchForObject(String.format(RequestConstants.TICKET_URI, chatId, ticketNumber),
                null, String.class));
        executeQuestion(responseMessage);

    }
}
