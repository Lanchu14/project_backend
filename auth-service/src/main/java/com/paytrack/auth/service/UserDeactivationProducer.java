package com.paytrack.auth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paytrack.auth.dto.UserDeactivationMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeactivationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.deactivate.queue}")
    private String queue;

    public void send(UserDeactivationMessage message) {

        rabbitTemplate.convertAndSend(
                queue,
                message
        );

    }
}