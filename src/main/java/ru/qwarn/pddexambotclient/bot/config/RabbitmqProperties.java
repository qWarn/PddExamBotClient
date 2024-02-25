package ru.qwarn.pddexambotclient.bot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq.notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class RabbitmqProperties {

    String routingKey;
    String queue;
    String exchange;

}
