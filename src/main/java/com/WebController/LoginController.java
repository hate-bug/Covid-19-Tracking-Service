package com.WebController;

import com.Model.ConfirmationToken;
import com.Model.User;
import com.Repository.ConfirmationTokenRepository;
import com.Repository.UserRepository;
import com.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String login (){
        return "loginpage";
    }

    @GetMapping(value = "/register")
    public String register(){
        return "register";
    }

    @GetMapping (value = "/admin")
    public String adminPage (){return "adminpage";}

    @RequestMapping(value = "/confirmaccount", method = RequestMethod.GET)
    public String ConfirmEmail (@RequestParam("token") String confirmationToken){
        return "confirmpage";
    }

    @PostMapping(value = "/userregister", consumes = "application/json")
    public ResponseEntity<String> userRegistration(@RequestBody User user, HttpServletRequest request){
        if (this.userRepository.findUserByEmailAddressIgnoreCase(user.getEmailAddress())!=null){
            return new ResponseEntity<>("user already exists", HttpStatus.FORBIDDEN);
        }
        this.userService.save(user, request);
        ConfirmationToken token = new ConfirmationToken(user);
        return new ResponseEntity<>("Confirmation email sent to: "+user.getEmailAddress(), HttpStatus.OK);
    }

    @PostMapping (value = "/posttoken")
    public ResponseEntity<String> verifytoken (@RequestBody String confirmationToken){
        if (this.userService.confirmToken(confirmationToken)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping (value = "/getemailwithtoken", produces = "application/json")
    public ResponseEntity<String> getUserByToken (@RequestParam("token") String token){
        if (this.confirmationTokenRepository.findByConfirmationToken(token)!=null){
            User user = this.confirmationTokenRepository.findByConfirmationToken(token).getUser();
            return new ResponseEntity<>(user.getEmailAddress(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping (value = "/userlogin", consumes = "application/json")
    public ResponseEntity<String> login (@RequestBody User longinUser){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(longinUser.getEmailAddress());
        if (user!=null){
            if (user.isEnabled()){
                return new ResponseEntity<>("Successfully log in", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Please verify you email address", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Email or password not correct.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping (value = "/isloggedin")
    public ResponseEntity<String> isloggedin (Authentication authentication){
        if (authentication!=null && authentication.isAuthenticated()){
            return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not logged in", HttpStatus.NOT_FOUND);
        }
    }
}
