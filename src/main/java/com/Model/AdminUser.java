package com.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class AdminUser extends User {

    public AdminUser (){
        super();
    }

    public AdminUser (String emailAddress, String password){
        super(emailAddress,password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        roles.add(new SimpleGrantedAuthority("ADMIN"));
        return roles ;
    }

}
