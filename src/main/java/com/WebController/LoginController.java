package com.WebController;

import com.Model.ConfirmationToken;
import com.Model.User;
import com.Model.UserSession;
import com.Repository.ConfirmationTokenRepository;
import com.Repository.UserRepository;
import com.Repository.UserSessionRepository;
import com.Service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @PostMapping(value = "/userregister", consumes = "application/json")
    public ResponseEntity<String> userRegistration(@RequestBody User user, HttpServletRequest request){
        if (this.userRepository.findUserByEmailAddressIgnoreCase(user.getEmailAddress())!=null){
            return new ResponseEntity<>("user already exists", HttpStatus.FORBIDDEN);
        }
        userRepository.save(user);
        ConfirmationToken token = new ConfirmationToken(user);
        String rootAddress = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        this.messageSender.sendRegisterEmail(user.getEmailAddress(), token.getConfirmationToken(), rootAddress);
        this.confirmationTokenRepository.save(token);
        return new ResponseEntity<>("Confirmation email sent to: "+user.getEmailAddress(), HttpStatus.OK);
    }

    @RequestMapping(value = "/confirmaccount", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ConfirmEmail (@RequestParam("token") String confirmationToken, Model model){
        ConfirmationToken token = this.confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ModelAndView modelview;
        if (token!=null){//valid user
            String emailAddress = token.getUser().getEmailAddress();
            User user = userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
            user.setEnable(true);
            userRepository.save(user);
            model.addAttribute("emailAddress", emailAddress);
            modelview = new ModelAndView("RegisterSuccess");
            modelview.setStatus(HttpStatus.OK);
        }else {
            modelview = new ModelAndView("Error");
            modelview.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelview;
    }

    @PostMapping (value = "/userlogin", consumes = "application/json")
    public ResponseEntity<String> login (@RequestBody User longinUser, HttpSession session, HttpServletRequest request){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(longinUser.getEmailAddress());
        if (user!=null){
            if (user.isEnabled()){
                UserSession userSession = new UserSession(user);
                request.getSession().setAttribute("usersessionid", userSession.getSessionId());
                this.userSessionRepository.save(userSession);
                return new ResponseEntity<>("Successfully log in", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Please verify you email address", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Email or password not correct.", HttpStatus.UNAUTHORIZED);
    }
}
