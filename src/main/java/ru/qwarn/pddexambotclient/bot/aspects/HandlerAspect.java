package ru.qwarn.pddexambotclient.bot.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class HandlerAspect {


    @Around(value = "execution(* ru.qwarn.pddexambotclient.bot.handlers.*.*(..))")
    public Object logHandleMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Starting execution of method {} with args [{}] ", proceedingJoinPoint.getSignature().getName(),
                proceedingJoinPoint.getArgs());

        Object object = proceedingJoinPoint.proceed();
        log.info("Ended execution of method {}", proceedingJoinPoint.getSignature().getName());
        return object;
    }


}
