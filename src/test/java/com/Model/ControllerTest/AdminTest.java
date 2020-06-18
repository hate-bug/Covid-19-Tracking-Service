package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Configuration.PasswordConfig;
import com.Model.AdminUser;
import com.Repository.AdminUserRepository;
import com.Repository.ConfirmationTokenRepository;
import com.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class AdminTest {

    @Autowired
    private PasswordConfig passwordConfig;

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
    public void AdminLoginTest () throws Exception {
        AdminUser adminUser = new AdminUser("covidtracking08@gmail.com", this.passwordConfig.passwordEncoder().encode("testpassword"));
        this.adminUserRepository.save(adminUser);
        RequestBuilder requestBuilder = formLogin().user("emailaddress","covidtracking08@gmail.com").password("password","testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder)
                .andExpect(authenticated().withUsername("covidtracking08@gmail.com"));

    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void adminPageTest () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="User",roles={"USER"})
    public void adminPageTestWithoutAdmin () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }
}