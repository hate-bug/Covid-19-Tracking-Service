package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {

    @Id
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne
    private User user;

    @Autowired
    public ConfirmationToken (){
        this(new User());
    }

    public ConfirmationToken (User user){
        this.user = user;
        this.createdDate = new Date();
        this.confirmationToken = UUID.randomUUID().toString();
    }

    public String getConfirmationToken (){
        return this.confirmationToken;
    }

    public User getUser (){
        return this.user;
    }
}
