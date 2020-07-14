package com.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String emailAddress;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "password", column = @Column(nullable = false))
    })
    private UserPassword userPassword;

    public User(){
        this("unknown", new UserPassword());
    }

    public User (String emailAddress, UserPassword userPassword){
        this.emailAddress = emailAddress;
        this.userPassword = userPassword;
    }

    public String getEmailAddress (){
        return this.emailAddress;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return roles ;
    }

    @Override
    public String getPassword() {
        return this.userPassword.getPassword();
    }

    @Override
    public String getUsername() {
        return this.emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUserPassword(UserPassword password){

        this.userPassword = password;
    }

    public long getId (){
        return this.id;
    }

}
