package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Configuration.PasswordConfig;
import com.Model.ConfirmationToken;
import com.Model.User;
import com.Repository.AdminUserRepository;
import com.Repository.ConfirmationTokenRepository;
import com.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class OAuthMVCTest {

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AdminUserRepository adminUserRepository;


    @Before
    public void pre (){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void RegisterTest () throws Exception {
        String userData = "{\"emailAddress\":\"covidtracking08@TEST.com\",\"password\":\"testpassword\"}";
        this.mockMvc.perform(post("/userregister").content(userData)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Confirmation email")));
        this.mockMvc.perform(post("/userregister").content(userData)
                .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("user already exists")));

    }

    @Test
    public void LoginTest () throws Exception {
        User user = new User("covidtracking08@gmail.com", passwordConfig.passwordEncoder().encode("testpassword"));
        ConfirmationToken token = new ConfirmationToken(user);
        this.userRepository.save(user);
        this.confirmationTokenRepository.save(token);
        assertFalse(user.isEnabled());
        RequestBuilder requestBuilder1 = formLogin().user("emailaddress","covidtracking08@gmail.com").password("password","testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder1)
                .andExpect(unauthenticated());
        this.mockMvc.perform(post("/posttoken").content(token.getConfirmationToken()))
                .andExpect(status().isCreated());
        assertTrue(userRepository.findUserByEmailAddressIgnoreCase("covidtracking08@gmail.com").isEnabled());
        RequestBuilder requestBuilder2 = formLogin().user("emailaddress","covidtracking08@gmail.com").password("password","testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder2)
                .andExpect(authenticated().withUsername("covidtracking08@gmail.com"));
    }

}
