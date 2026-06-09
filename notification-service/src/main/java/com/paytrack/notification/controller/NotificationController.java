package com.paytrack.notification.controller;

import org.springframework.web.bind.annotation.*;

import com.paytrack.notification.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public String sendEmail(
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message){

        emailService.sendEmail(
                email,
                subject,
                message);

        return "Email Sent";
    }
}