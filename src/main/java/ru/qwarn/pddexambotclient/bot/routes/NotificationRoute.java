package ru.qwarn.pddexambotclient.bot.routes;

import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.qwarn.pddexambotclient.bot.executors.NotificationExecutor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationRoute extends RouteBuilder {

    private final NotificationExecutor notificationExecutor;

    @Override
    public void configure() throws Exception {
        from("spring-rabbitmq:pdd_exam_exchange?queues=notification_queue&routingKey=notification_key")
                .routeId("Rabbitmq consumer route")
                .log(LoggingLevel.DEBUG, "Started execution of rabbitmq consumer route")
                .log(LoggingLevel.DEBUG, "Received ${body}")
                .unmarshal(new ListJacksonDataFormat(SendMessage.class))
                .process(exchange ->
                        notificationExecutor.executeNotification(
                                exchange.getIn().getBody(List.class)));
    }
}
