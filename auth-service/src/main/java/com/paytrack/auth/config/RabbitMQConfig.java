package com.paytrack.auth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE =
            "forgot-password-queue";

    @Bean
    public Queue forgotPasswordQueue() {

        return new Queue(QUEUE);
    }
}