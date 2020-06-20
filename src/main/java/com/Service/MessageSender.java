package com.Service;

import com.Model.Applicant;
import com.Model.User;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class MessageSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSender (){}

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Autowired
    private UserRepository userRepository;

    public User sendRegisterEmail(Applicant applicant){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(applicant.getApplicantEmail());
        message.setSubject("COVID-19 tracking system account.");
        message.setFrom(this.senderEmailAddress);
        String password = UUID.randomUUID().toString();
        User user = new User(applicant.getApplicantEmail(), password);
        this.userRepository.save(user);
        message.setText("Your account password is:"
                + password + "\n You can change your password after logged in.");
        javaMailSender.send (message);
        return user;
    }
}
