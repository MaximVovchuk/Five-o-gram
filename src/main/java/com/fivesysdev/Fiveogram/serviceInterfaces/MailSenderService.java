package com.fivesysdev.Fiveogram.serviceInterfaces;


import jakarta.mail.MessagingException;

public interface MailSenderService {
    void sendMessage(String subject, String text, String recipient) throws MessagingException;
}
