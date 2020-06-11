package com.Model;

import com.Repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String sessionId;

    @OneToOne
    private User user;

    private Date date;

    @Autowired
    public UserSession(){
        this(new User());
    }

    public UserSession(User user){
        this.user = user;
        this.date = new Date();
        this.sessionId = UUID.randomUUID().toString();
    }

    public User getUser (){
        return this.user;
    }

    public String getSessionId(){
        return this.sessionId;
    }

}
