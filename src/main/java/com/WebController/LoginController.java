package com.WebController;

import com.Model.User;
import com.Model.UserPassword;
import com.Repository.UserRepository;
import com.Payload.ConfirmPasswordPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (authentication!=null && authentication.isAuthenticated() && this.userRepository.findUserByEmailAddressIgnoreCase(authentication.getName())!=null){
            return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not logged in", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping (value = "/changepassword")
    public boolean changePassword (@RequestBody ConfirmPasswordPayload confirmPasswordPayload, Principal principal){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName());
        if (!confirmPasswordPayload.getConfirmnewPassword().equals(confirmPasswordPayload.getNewPassword())){
            return false;
        }
        if (!new BCryptPasswordEncoder().matches(confirmPasswordPayload.getOldPassword(), user.getPassword())){ //check old password
            return false;
        }
        UserPassword password = new UserPassword(confirmPasswordPayload.getNewPassword());
        user.setUserPassword(password);
        this.userRepository.save(user);
        return true;
    }

}
