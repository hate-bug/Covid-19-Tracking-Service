package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class OAuthMVCTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;


    @Before
    public void pre (){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void LoginTest () throws Exception {
        RequestBuilder requestBuilder1 = formLogin().user("emailaddress", this.senderEmailAddress).password("password", "testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder1)
                .andExpect(unauthenticated());
    }

}
