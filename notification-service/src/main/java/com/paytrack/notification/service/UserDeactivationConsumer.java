package com.paytrack.notification.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.paytrack.notification.dto.UserDeactivationMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDeactivationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.deactivate.queue}")
    public void receive(UserDeactivationMessage message) {

        String subject = "PayTrack Account Deactivated";

        String body =
                "Hello " + message.getUsername() + ",\n\n"

                        + "Your PayTrack account has been deactivated because your account was marked inactive by the administrator.\n\n"

                        + "Please contact the administrator for reactivation.\n\n"

                        + "Administrator Email : "
                        + message.getAdminEmail()

                        + "\n\nRegards,\nPayTrack Team";

        emailService.sendEmail(

                message.getEmail(),

                subject,

                body

        );

    }

}