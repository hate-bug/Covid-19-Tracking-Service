package com.Configuration;


import com.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfiguration (PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private PasswordConfig passwordConfig;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Value("${spring.mail.password}")
    private String password;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                    .and()
                    .formLogin().loginPage("/loginpage")
                    .loginProcessingUrl("/userlogin")
                    .usernameParameter("emailaddress").passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                .and()
                    .logout().permitAll()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                    .httpBasic()
                .and()
                    .csrf().disable();

    }

}
