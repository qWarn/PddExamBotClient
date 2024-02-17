package ru.qwarn.pddexambotclient.bot.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.qwarn.pddexambotclient.bot.TelegramBot;
import ru.qwarn.pddexambotclient.bot.botutils.ExceptionMessageCreator;

@Lazy
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExecutorAspect {

    private final TelegramBot telegramBot;

    @Pointcut("execution(* ru.qwarn.pddexambotclient.bot.executors.*.execute*(..))")
    public void executorPointcut(){}

    @Before("executorPointcut()")
    public void logBefore(JoinPoint joinPoint){
        log.info("Starting execution of method {} with args {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "executorPointcut() && args(long, ..)", throwing = "exception")
    public void logAndExecuteAfterThrowing(JoinPoint joinPoint, HttpClientErrorException exception) throws TelegramApiException {
        long chatId = (long) joinPoint.getArgs()[0];

        log.warn("Caught client error with message: {}", exception.getMessage());
        telegramBot.execute(ExceptionMessageCreator.createExceptionMessage(chatId, exception));
    }

    @After("executorPointcut()")
    public void logAfter(JoinPoint joinPoint){
        log.info("Ended execution of method {} with args {}"
                , joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}
