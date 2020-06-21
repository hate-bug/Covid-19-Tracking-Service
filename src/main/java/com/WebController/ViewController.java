package com.WebController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping(value = "/login")
    public String loginpage (){
        return "login";
    }

    @GetMapping (value = "/changepassword")
    public String changepwdpage (){
        return "changepassword";
    }

    @GetMapping (value = "/admin")
    public String adminPage (){
        return "/admin";
    }
}
