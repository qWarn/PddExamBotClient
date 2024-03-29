package ru.qwarn.pddexambotclient.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.qwarn.pddexambotclient.bot.aspects.ExecutorAspect;
import ru.qwarn.pddexambotclient.bot.config.TelegramConfig;
import ru.qwarn.pddexambotclient.bot.executors.NotificationExecutor;
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
    private final NotificationExecutor notificationExecutor;
    private final ExecutorAspect executorAspect;

    public TelegramBot(TelegramConfig telegramConfig, CallbackHandler callbackHandler,
                       MessageHandler messageHandler, QuestionExecutor questionExecutor,
                       TicketExecutor ticketExecutor, NotificationExecutor notificationExecutor, ExecutorAspect executorAspect) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.callbackHandler = callbackHandler;
        this.messageHandler = messageHandler;
        this.questionExecutor = questionExecutor;
        this.ticketExecutor = ticketExecutor;
        this.notificationExecutor = notificationExecutor;
        this.executorAspect = executorAspect;
    }

    @PostConstruct
    public void initExecutors() {
        questionExecutor.setTelegramBot(this);
        ticketExecutor.setTelegramBot(this);
        notificationExecutor.setTelegramBot(this);
        executorAspect.setTelegramBot(this);
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText() && !update.getMessage().getText().isEmpty()) {
            messageHandler.handle(update);
        } else if (update.hasCallbackQuery()) {
            callbackHandler.handle(update);
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
