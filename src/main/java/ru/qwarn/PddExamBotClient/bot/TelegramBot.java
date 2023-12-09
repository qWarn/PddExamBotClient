package ru.qwarn.PddExamBotClient.bot;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.PddExamBotClient.bot.config.TelegramConfig;
import ru.qwarn.PddExamBotClient.bot.executors.QuestionExecutor;
import ru.qwarn.PddExamBotClient.bot.executors.TicketExecutor;
import ru.qwarn.PddExamBotClient.bot.handlers.CallbackHandler;
import ru.qwarn.PddExamBotClient.bot.handlers.MessageHandler;

@Component
public class TelegramBot extends TelegramWebhookBot {

    private final TelegramConfig telegramConfig;
    private final CallbackHandler callbackHandler;
    private final MessageHandler messageHandler;
    private final QuestionExecutor questionExecutor;
    private final TicketExecutor ticketExecutor;

    @Autowired
    public TelegramBot(TelegramConfig telegramConfig, CallbackHandler callbackHandler,
                       MessageHandler messageHandler, QuestionExecutor questionExecutor,
                       TicketExecutor ticketExecutor)
    {
        super(telegramConfig.getBotToken());
        this.telegramConfig = telegramConfig;
        this.callbackHandler = callbackHandler;
        this.messageHandler = messageHandler;
        this.questionExecutor = questionExecutor;
        this.ticketExecutor = ticketExecutor;
    }

    @PostConstruct
    public void initExecutors(){
        questionExecutor.setTelegramBot(this);
        ticketExecutor.setTelegramBot(this);
    }


    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText() && !update.getMessage().getText().isEmpty()){
            messageHandler.handle(update);
        }else if (update.hasCallbackQuery()){
            callbackHandler.handle(update);
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
