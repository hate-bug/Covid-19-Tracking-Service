package com.Application;

import com.Configuration.SecurityConfiguration;
import com.Service.EmailSenderService;
import com.WebController.EventController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackageClasses = EventController.class)
@ComponentScan(basePackageClasses = EmailSenderService.class)
@ComponentScan(basePackageClasses = SecurityConfiguration.class)
@EnableJpaRepositories(basePackages = "com.Repository")
@EntityScan("com.Model")
public class Tracking_System_Application {

    public static void main (String[] args){
        SpringApplication.run(Tracking_System_Application.class, args);
    }
}
