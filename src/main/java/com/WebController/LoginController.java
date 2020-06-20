package com.WebController;

import com.Model.User;
import com.Repository.UserRepository;
import com.Payload.ConfirmPasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping (value = "/isloggedin")
    public ResponseEntity<String> isloggedin (Authentication authentication){
        if (authentication!=null && authentication.isAuthenticated()){
            return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not logged in", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping (value = "/changepassword")
    public boolean changePassword (@RequestBody ConfirmPasswordEntity confirmPasswordEntity, Principal principal){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName());
        if (!confirmPasswordEntity.getConfirmnewPassword().equals(confirmPasswordEntity.getNewPassword())){
            return false;
        }
        if (!new BCryptPasswordEncoder().matches(confirmPasswordEntity.getOldPassword(), user.getPassword())){ //check old password
            return false;
        }
        user.setPassword(confirmPasswordEntity.getNewPassword());
        this.userRepository.save(user);
        return true;
    }

}
