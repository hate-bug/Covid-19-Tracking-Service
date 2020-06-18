package com.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Inheritance (strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String emailAddress;
    private String password; //Encrypted password
    private boolean isEnabled; //False if user has not confirmed email address yet
    private boolean isVerified;
    @OneToOne
    @Nullable
    private UserFile userFile;

    @Autowired
    public User(){
        this("unknown", "unknown");
    }

    public User (String emailAddress, String password){
        this.emailAddress = emailAddress;
        this.password = password;
        this.isEnabled = false;
        this.isVerified = false;
    }

    public String getEmailAddress (){
        return this.emailAddress;
    }

    public String getPassword (){
        return this.password;
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

    public boolean isVerified (){return this.isVerified;}

    public void setVerified (boolean isVerified) {this.isVerified=isVerified;}

    public void addUserFile (UserFile userFile){
        this.userFile = userFile;
    }

    public Optional<UserFile> getUserFile (){
        return Optional.ofNullable(this.userFile);
    }
}
