package com.Configuration;

import com.Model.AdminUserEntity;
import com.Model.UserPassword;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Value("${admin_user}")
    private String adminuser;

    @Value("${admin_password}")
    private String plainPasswordString;

    @Override
    public void run(String... args) throws Exception {
        if (this.userRepository.findUserByEmailAddressIgnoreCase(adminuser) == null){
            UserPassword userPassword = new UserPassword(this.plainPasswordString);
            AdminUserEntity adminUser = new AdminUserEntity(this.adminuser, userPassword);
            this.userRepository.save(adminUser);
        }
    }
}
