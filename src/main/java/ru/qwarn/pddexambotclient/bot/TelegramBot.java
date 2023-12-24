package ru.qwarn.pddexambotclient.bot;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.botutils.ExceptionMessageCreator;
import ru.qwarn.pddexambotclient.bot.config.TelegramConfig;
import ru.qwarn.pddexambotclient.bot.executors.QuestionExecutor;
import ru.qwarn.pddexambotclient.bot.executors.TicketExecutor;
import ru.qwarn.pddexambotclient.bot.handlers.CallbackHandler;
import ru.qwarn.pddexambotclient.bot.handlers.MessageHandler;

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
                       TicketExecutor ticketExecutor) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.callbackHandler = callbackHandler;
        this.messageHandler = messageHandler;
        this.questionExecutor = questionExecutor;
        this.ticketExecutor = ticketExecutor;
    }

    @PostConstruct
    public void initExecutors() {
        questionExecutor.setTelegramBot(this);
        ticketExecutor.setTelegramBot(this);
    }


    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText() && !update.getMessage().getText().isEmpty()) {
            try {
                messageHandler.handle(update);
            } catch (HttpClientErrorException e) {
                execute(ExceptionMessageCreator.createExceptionMessage(update.getMessage().getChatId(), e));
            }
        } else if (update.hasCallbackQuery()) {
            try {
                callbackHandler.handle(update);
            } catch (HttpClientErrorException e) {
                execute(ExceptionMessageCreator.createExceptionMessage(update.getCallbackQuery().getMessage().getChatId(), e));
            }
        }
        return null;
    }


    @Override
    public String getBotPath() {
        return telegramConfig.getBotUri();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }
}
