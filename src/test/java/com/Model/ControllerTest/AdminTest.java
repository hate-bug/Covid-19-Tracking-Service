package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Configuration.PasswordConfig;
import com.Model.AdminUser;
import com.Model.Applicant;
import com.Repository.ApplicantRepository;
import com.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;
import static junit.framework.TestCase.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UserRepository userRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Before
    public void pre (){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void AdminLoginTest () throws Exception {
        AdminUser adminUser = new AdminUser("test@test.test", "testpassword");
        this.userRepository.save(adminUser);
        RequestBuilder requestBuilder = formLogin().user("emailaddress","test@test.test").password("password","testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder)
                .andExpect(authenticated().withUsername("test@test.test").withRoles("ADMIN", "USER"));

    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void adminPageTest () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void adminPageTestWithoutAdmin () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void adminAppprove () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        String data = "\"" + String.valueOf(id) + "\"";
        this.mockMvc.perform(post("/admin/approveapplicant").content(data).contentType("application/json"))
                .andExpect(status().isOk());
        assertEquals (false, this.applicantRepository.existsById(id));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admindDecline () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        String data = String.valueOf(id);
        this.mockMvc.perform(delete("/admin/deleteapplicant?id="+data))
                .andExpect(status().isOk());
        assertEquals(false, this.applicantRepository.existsById(id));
    }
}
