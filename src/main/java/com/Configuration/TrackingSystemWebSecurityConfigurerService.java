package com.Configuration;

import com.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
public class TrackingSystemWebSecurityConfigurerService extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(myUserDetailService)
                .passwordEncoder(this.passwordConfig.passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/admin/**").access("hasRole('ADMIN')")
                    .and()
                    .formLogin().loginPage("/loginpage")
                    .loginProcessingUrl("/userlogin")
                    .usernameParameter("emailaddress").passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureForwardUrl("/login")
                .and()
                    .logout().permitAll()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                    .csrf().disable();

    }

}
