package ru.qwarn.pddexambotclient.bot.executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.dto.TaskDTO;

import java.util.HashMap;

import static ru.qwarn.pddexambotclient.bot.botutils.RequestConstants.*;

@Component
@RequiredArgsConstructor
@Setter
public class QuestionExecutor {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private TelegramBot telegramBot;

    public void executeQuestionAnswerAndNextQuestion(long chatId, String answerNumber) throws TelegramApiException, JsonProcessingException {
        if (answerNumber.matches("^-?\\d+")) {
                telegramBot.execute(restTemplate.patchForObject(String.format(ANSWER_URI, chatId, answerNumber), null, SendMessage.class));
                executeQuestion(restTemplate.exchange(
                        String.format(QUESTION_URI, chatId), HttpMethod.PATCH, null, String.class, new HashMap<>()
                ));
        } else {
            telegramBot.execute(SendMessage.builder().text("Выберите число.").chatId(chatId).build());
        }
    }

    private void executeQuestion(ResponseEntity<String> response) throws JsonProcessingException, TelegramApiException {
        if (response.getHeaders().get("MessageType").get(0).equals("sendphoto")) {
            telegramBot.execute(objectMapper.readValue(response.getBody(), SendPhoto.class));
        } else {
            telegramBot.execute(objectMapper.readValue(response.getBody(), SendMessage.class));
        }
    }

    public void executeFirstQuestionFromSelected(long chatId) throws TelegramApiException, JsonProcessingException {
        executeQuestion(restTemplate.exchange(
                RequestEntity.patch(String.format(QUESTION_URI, chatId)).accept(MediaType.APPLICATION_JSON).body(new TaskDTO("selected", null)),
                String.class
        ));
    }

    public void executeFirstQuestionFromTicket(long chatId, int ticketNumber) throws TelegramApiException, JsonProcessingException {
        executeQuestion(restTemplate.exchange(
                RequestEntity.patch(String.format(QUESTION_URI, chatId)).accept(MediaType.APPLICATION_JSON).body(new TaskDTO("ticket", ticketNumber)),
                String.class
        ));
    }



}
