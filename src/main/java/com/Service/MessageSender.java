package com.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSender (){}

    @Value("${spring.mail.username}")
    private String senderEmailAddress;


    public void sendRegisterEmail(String emailAddress, String token, String serverAddress){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress);
        message.setSubject("COVID-19 Tracking System Registration.");
        message.setFrom(this.senderEmailAddress);
        message.setText("To confirm your email address, please click here:"
                + serverAddress +"/confirmaccount?token="+token);
        javaMailSender.send (message);
    }
}
