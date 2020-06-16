package com.Model;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminUser extends User {

    @Id
    private long id;

    @Autowired
    public AdminUser (){
        super();
    }

    public AdminUser (String emailAddress, String password){
        super(emailAddress,password);
    }

    @Override //Admin user should not need register, admin account should be assigned directly.
    public boolean isEnabled (){//admin account should always be enabled
        return true;
    }

}
