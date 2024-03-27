package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String inviteCode,
                          String creator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("splitty35@gmail.com");
        message.setCc("splitty35@gmail.com");
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
