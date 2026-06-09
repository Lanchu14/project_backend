package com.paytrack.expense.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.paytrack.expense.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseProducer {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ExpenseProducer.class
            );

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE,
                message
        );

        if (logger.isInfoEnabled()) {

            logger.info(
                    "Message Sent: {}",
                    message
            );
        }
    }
}