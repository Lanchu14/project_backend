package com.paytrack.notification.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paytrack.notification.service.ForgotPasswordConsumer;

@Configuration
public class RabbitMQConfig {

    @Bean
    public SimpleMessageListenerContainer container(
            ConnectionFactory connectionFactory,
            ForgotPasswordConsumer consumer) {

        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();

        container.setConnectionFactory(
                connectionFactory
        );

        container.setQueueNames(
                "forgot-password-queue"
        );

        container.setAcknowledgeMode(
                AcknowledgeMode.AUTO
        );

        container.setMessageListener(message -> {

            String msg =
                    new String(
                            message.getBody()
                    );

            consumer.processMessage(msg);
        });

        return container;
    }
}