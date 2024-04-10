package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailSenderService {
    private static final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    public void sendInvitationEmail(String senderEmail,
                          String toEmail,
                          String inviteCode,
                          String creator,
                          String password,
                          String host,
                          int port,
                          boolean smtpAuth,
                          boolean startTls) {
        System.out.println(password);
        System.out.println(host);
        System.out.println(senderEmail);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setPassword(password);
        mailSender.setUsername(senderEmail);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTls);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setCc(senderEmail);
        message.setTo(toEmail);
        String body = "  Good evening,\n \n  You have been invited to a new event on Splitty by "
                + creator
                + ". Make sure to install the app, then join your friend using the following invite code: "
                + inviteCode
                + ".\n \n  Best wishes,\nSplitty (OOPP Team 35)";
        message.setText(body);
        message.setSubject("You have been invited to an event on Splitty!");

        mailSender.send(message);
    }
}
