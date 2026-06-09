package com.paytrack.notification.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordConsumer {

    private final EmailService emailService;

    public void processMessage(String msg) {

        System.out.println(
                "MESSAGE RECEIVED"
        );

        System.out.println(msg);

        String[] lines = msg.split("\n");

        String email = "";

        for (String line : lines) {

            if (line.startsWith("Email:")) {

                email = line.replace(
                        "Email:",
                        ""
                ).trim();
            }
        }

        emailService.sendEmail(
                email,
                "PayTrack Forgot Password",
                msg
        );

        System.out.println(
                "EMAIL SENT SUCCESSFULLY"
        );
    }
}