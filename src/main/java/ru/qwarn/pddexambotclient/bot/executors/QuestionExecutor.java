package ru.qwarn.pddexambotclient.bot.executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;

import java.util.HashMap;

import static ru.qwarn.pddexambotclient.bot.constants.RequestConstants.*;
@Component
@RequiredArgsConstructor
@Setter
public class QuestionExecutor {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private TelegramBot telegramBot;

    public void executeQuestion(long chatId, String answerNumber, int messageId) throws TelegramApiException, JsonProcessingException {
        deleteMessage(chatId, messageId);
        if (answerNumber != null) {
            restTemplate.patchForObject(String.format(CHECK_ANSWER_URI, chatId, answerNumber), null, SendMessage.class);
        }

        ResponseEntity<String> questionResponse = restTemplate.exchange(
                String.format(GET_QUESTION_URI, chatId), HttpMethod.PATCH, null, String.class, new HashMap<>()
        );

        if (questionResponse.getHeaders().get("MessageType").get(0).equals("sendphoto")) {
            telegramBot.execute(objectMapper.readValue(questionResponse.getBody(), SendPhoto.class));
        } else {
            telegramBot.execute(objectMapper.readValue(questionResponse.getBody(), SendMessage.class));
        }
    }

    public void executeAnswer(long chatId, int messageId) throws TelegramApiException {
        deleteMessage(chatId, messageId);
        telegramBot.execute(
                restTemplate.getForObject(String.format(GET_CORRECT_ANSWER_URI, chatId), SendMessage.class)
        );
    }

    public void addQuestionToSelected(long chatId, String questionId) {
        restTemplate.patchForObject(String.format(ADD_TO_SELECTED_URI, chatId, questionId), null, String.class);
    }

    public void removeFromSelectedList(long chatId, String questionId) {
        restTemplate.patchForObject(String.format(REMOVE_FROM_SELECTED_URI, chatId, questionId), null, String.class);
    }

    private void deleteMessage(long chatId, int messageId) throws TelegramApiException {
        telegramBot.execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build());
    }
}
