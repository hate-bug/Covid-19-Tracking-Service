package com.Model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserPassword implements Serializable {

    private String encryptedPassword;

    public UserPassword (){
        this("unknown");
    }

    public UserPassword (String plainTextPassword){
        this.encryptedPassword = new BCryptPasswordEncoder().encode(plainTextPassword);
    }

    public void setPassword (String plainTextPassword){
        this.encryptedPassword = new BCryptPasswordEncoder().encode(plainTextPassword);
    }

    public String getPassword (){
        return this.encryptedPassword;
    }

}
