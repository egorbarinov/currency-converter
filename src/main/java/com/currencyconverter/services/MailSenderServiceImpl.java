package com.currencyconverter.services;

import com.currencyconverter.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    @Value("${server.port}")
    private int port;
    @Value("${server.hostname}")
    private String hostname;


    public MailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendActivateCode(User user) {
        String subject = "Please activate your account";
        String content = "Please activate your account. Go to the link: \n"
                + "http://" + hostname + ":" + port + "/register/activate/" + user.getActivateCode();

        sendMail(user.getEmail(), subject, content);
    }

    private void sendMail(String email, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("egor.barinov@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

}
