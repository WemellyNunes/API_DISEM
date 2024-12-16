package com.disem.API.services;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final EmailService emailService;

    public EmailNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendEmailAsync(String to, String subject, String message) {
        try {
            emailService.sendSimpleMail(to, subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
