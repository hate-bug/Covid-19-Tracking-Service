package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String emailAddress;
    private String password;
    private boolean isEnabled; //False if user has not confirmed email address yet

    @Autowired
    public User(){
        this("unknown", "unknown");
    }

    public User (String emailAddress, String password){
        this.emailAddress = emailAddress;
        this.password = password;
        this.isEnabled = false;
    }

    public String getEmailAddress (){
        return this.emailAddress;
    }

    public String getPassword (){
        return this.password;
    }

    public void setEmailAddress (String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword (String password){
        this.password = password;
    }

    public long getId (){
        return this.id;
    }

    public boolean isEnabled (){
        return this.isEnabled;
    }

    public void setEnable (boolean isEnabled){
        this.isEnabled = isEnabled;
    }


}
