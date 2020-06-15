package com.Service;

import com.Model.ConfirmationToken;
import com.Model.User;
import com.Repository.ConfirmationTokenRepository;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void save (User user, HttpServletRequest request){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        ConfirmationToken token = new ConfirmationToken(user);
        String rootAddress = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        this.messageSender.sendRegisterEmail(user.getEmailAddress(), token.getConfirmationToken(), rootAddress);
        this.confirmationTokenRepository.save(token);
    }

    public boolean confirmToken (String confirmationToken){
        ConfirmationToken token = this.confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null){
            String emailAddress = token.getUser().getEmailAddress();
            User user = userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
            user.setEnable(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

}
