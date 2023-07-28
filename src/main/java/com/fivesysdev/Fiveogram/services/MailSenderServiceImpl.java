package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.serviceInterfaces.MailSenderService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    private final Session session;

    MailSenderServiceImpl() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        this.session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void sendMessage(String subject, String text, String recipient) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setText(text);
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress("no-reply@gmail.com"));
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
