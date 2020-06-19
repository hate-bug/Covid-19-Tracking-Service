package com.WebController;

import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;;

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

}
