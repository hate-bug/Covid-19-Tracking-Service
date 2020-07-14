package com.Configuration;

import com.Model.AdminUser;
import com.Model.UserPassword;
import com.Repository.ApplicantRepository;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Value("${admin_user}")
    private String adminuser;

    @Value("${admin_password}")
    private String plainPasswordString;

    @Override
    public void run(String... args) throws Exception {
        UserPassword userPassword = new UserPassword(this.plainPasswordString);
        AdminUser adminUser = new AdminUser(this.adminuser, userPassword);
        this.userRepository.save(adminUser);
    }
}
