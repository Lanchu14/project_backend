package com.paytrack.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.paytrack.auth.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordProducer {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ForgotPasswordProducer.class
            );

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE,
                message
        );

        if (logger.isInfoEnabled()) {

            logger.info(
                    "Message Sent : {}",
                    message
            );
        }
    }
}