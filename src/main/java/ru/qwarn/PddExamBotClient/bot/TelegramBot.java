package ru.qwarn.PddExamBotClient.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.PddExamBotClient.bot.botutils.ExceptionMessageCreator;
import ru.qwarn.PddExamBotClient.bot.botutils.RequestConstants;
import ru.qwarn.PddExamBotClient.bot.config.TelegramConfig;

import java.util.Objects;

@Component
public class TelegramBot extends TelegramWebhookBot {

    private final TelegramConfig telegramConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ExceptionMessageCreator exceptionMessageCreator;

    @Autowired
    public TelegramBot(TelegramConfig telegramConfig, ObjectMapper objectMapper, RestTemplate restTemplate, ExceptionMessageCreator exceptionMessageCreator){
        super(telegramConfig.getBotToken());
        this.telegramConfig = telegramConfig;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.exceptionMessageCreator = exceptionMessageCreator;
    }

    public void handleQuestion(String responseMessage) throws JsonProcessingException, TelegramApiException {
            String methodName = StringUtils.substringBefore(StringUtils.substringAfter(responseMessage, "\"method\":\""), "\"}");
            if (methodName.equals("sendphoto")) {
                    execute(objectMapper.readValue(responseMessage, SendPhoto.class));
            } else {
                    execute(objectMapper.readValue(responseMessage, SendMessage.class));
            }
    }

    public void handleQuestionAnswer(long chatId, String answerNumber) throws TelegramApiException, JsonProcessingException {
        if (answerNumber.matches("\\d")) {
            try {
                execute(restTemplate.getForObject(String.format(RequestConstants.ANSWER_URI, chatId, answerNumber), SendMessage.class));
                handleQuestion(restTemplate.patchForObject(String.format(RequestConstants.QUESTION_URI, chatId), null, String.class));
            }catch (HttpClientErrorException e){
                execute(exceptionMessageCreator.createExceptionMessage(chatId, e));
            }
        }
    }

    public void sendSelectedQuestionIfExists(long chatId) throws TelegramApiException, JsonProcessingException {
        try {
            handleQuestion(Objects.requireNonNull(restTemplate.patchForObject(String.format(RequestConstants.SELECTED_URI, chatId), null, String.class)));
        } catch (HttpClientErrorException e) {
            execute(exceptionMessageCreator.createExceptionMessage(chatId, e));
        }
    }


    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && !update.getMessage().getText().isEmpty()){
            String messageFromUser = update.getMessage().getText().toLowerCase();
            long chatId = update.getMessage().getChatId();

            switch (messageFromUser){
                case "/start" ->
                    execute(restTemplate.postForObject(String.format(RequestConstants.START_URI, chatId),
                            null, SendMessage.class));

                case "выйти" ->
                        execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId),
                                null, SendMessage.class));

                default ->
                        handleQuestionAnswer(chatId, messageFromUser);
            }
        }else if (update.hasCallbackQuery()){
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callBackData.split(" ")[0]){
                case "ticket" ->
                    handleQuestion(Objects.requireNonNull(restTemplate.patchForObject(String.format(RequestConstants.TICKET_URI, chatId, callBackData.split(" ")[1]),
                            null, String.class)));

                case "selected" ->
                    sendSelectedQuestionIfExists(chatId);

                case "backToTickets" ->
                        execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId),
                                null, SendMessage.class));

                case "addToSelected" ->
                        restTemplate.patchForObject(String.format(RequestConstants.ADD_TO_SELECTED_URI, chatId, callBackData.split(" ")[1]),
                                null, String.class);

                case "removeFromSelected" ->
                        restTemplate.patchForObject(String.format(RequestConstants.REMOVE_FROM_SELECTED_URI, chatId, callBackData.split(" ")[1]),
                                null, String.class); //TODO do deleteMapping

                case "nextTickets" ->
                        execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId) + RequestConstants.TICKET_NEXT_PARAM,
                                null, SendMessage.class));

                case "prevTickets" ->
                        execute(restTemplate.patchForObject(String.format(RequestConstants.TICKETS_URI, chatId) + RequestConstants.TICKET_PREV_PARAM,
                                null, SendMessage.class));
            }
        }
        return null;
    }


    @Override
    public String getBotPath() {
        return telegramConfig.getWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }
}
