package com.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class AdminUserEntity extends User_Entity {

    public AdminUserEntity(){
        super();
    }

    public AdminUserEntity(String emailAddress, UserPassword password){
        super(emailAddress,password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roles ;
    }

}
